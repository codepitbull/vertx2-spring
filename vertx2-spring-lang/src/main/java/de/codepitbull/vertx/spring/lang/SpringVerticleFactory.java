package de.codepitbull.vertx.spring.lang;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Container;
import org.vertx.java.platform.Verticle;
import org.vertx.java.platform.VerticleFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A VerticleFactory based around Spring. A SINGLE application context is loaded and used to create Verticles.
 * To use it create a module with a main pointing to {@see org.springframework.context.annotation.Configuration} annotated class like this:<br/>
 * "main":"spring:de.codepitbull.vertx.spring.lang.demo.TestConfiguration"<br/>
 * The Configuration will be used to initialize the spring context.
 * After this is done you can now deploy verticles prefixed with"spring:". This leads to the verticles being looked up from the spring application context.
 *
 * To register the language implementation by hand do the following:<br/>
 * System.setProperty("vertx.langs.spring", "de.codepitbull~spring-lang~1.0.0-final:de.codepitbull.vertx.spring.lang.SpringVerticleFactory");
 * System.setProperty("spring.context", "de.codepitbull.vertx.ConfigurationTest");
 *
 * pm.installModule("de.codepitbull~spring-lang~1.0.0-final", new Handler<AsyncResult<Void>>() {<br/>
 *  @Override<br/>
 *  public void handle(AsyncResult<Void> event) {<br/>
 *   DO STUFF HERE!<br/>
 *  }<br/>
 * }<br/>
 *
 * @author Jochen Mader
 */
public class SpringVerticleFactory implements VerticleFactory {
    private ClassLoader cl;
    private Vertx vertx;
    private Container container;
    protected static AtomicReference<AnnotationConfigApplicationContext> annotationConfigApplicationContextRef = new AtomicReference<AnnotationConfigApplicationContext>(null);

    @Override
    public void init(Vertx vertx, Container container, ClassLoader cl) {
        this.cl = cl;
        this.vertx = vertx;
        this.container = container;
    }

    public Verticle createVerticle(String main) throws Exception {

        if(annotationConfigApplicationContextRef.get() == null) {
            Class clazz;
            try {
                clazz = cl.loadClass(main);
            } catch (ClassNotFoundException e) {
                throw new ClassNotFoundException("Maybe you are trying to register a Spring-Verticle before the context is up?", e);
            }
            if (clazz.getAnnotation(Configuration.class) != null) {
                GenericApplicationContext genericApplicationContext = new GenericApplicationContext();
                genericApplicationContext.setClassLoader(cl);
                ConfigurableListableBeanFactory beanFactory = genericApplicationContext.getBeanFactory();
                registerVertxBeans(beanFactory);
                genericApplicationContext.refresh();
                genericApplicationContext.start();
                AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
                annotationConfigApplicationContext.setParent(genericApplicationContext);
                annotationConfigApplicationContext.register(clazz);
                annotationConfigApplicationContext.refresh();

                if(annotationConfigApplicationContextRef.compareAndSet(null, annotationConfigApplicationContext)) {
                    return createActualVerticle(annotationConfigApplicationContext);
                }
                throw new RuntimeException("Currently only one application context is supported. Looks like you tried to create a second one.");
            }
        }

        return getBeanByNameFromApplicationContext(main);
    }

    private Verticle getBeanByNameFromApplicationContext(String main) {
        return (Verticle)annotationConfigApplicationContextRef.get().getBean(main);
    }

    private SpringContextHolderVerticle createActualVerticle(AnnotationConfigApplicationContext annotationConfigApplicationContext) {
        SpringContextHolderVerticle verticle = new SpringContextHolderVerticle(annotationConfigApplicationContext);
        verticle.setVertx(vertx);
        verticle.setContainer(container);
        return verticle;
    }

    private void registerVertxBeans(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.registerSingleton(Vertx.class.getName(), vertx);
        beanFactory.registerAlias(Vertx.class.getName(), "vertx");
        beanFactory.registerSingleton(Container.class.getName(), container);
        beanFactory.registerAlias(Container.class.getName(), "container");
        beanFactory.registerSingleton(EventBus.class.getName(), vertx.eventBus());
        beanFactory.registerAlias(EventBus.class.getName(), "eventBus");
    }

    public void reportException(Logger logger, Throwable t) {
        logger.error("Exception in Java verticle", t);
    }

    public void close() {
    }
}
