package de.codepitbull.vertx.spring.lang;

import org.springframework.context.support.GenericApplicationContext;
import org.vertx.java.platform.Verticle;

/**
 * This Verticle holds the Spring-Application-Context to be used. It is responsible for all related life-cycle events.
 * You should NOT create this Verticle directly. This is done by {@see de.codepitbull.vertx.spring.lang.SpringVerticleFactory}
 *
 * @author Jochen Mader
 */
public class SpringContextHolderVerticle extends Verticle {
    private GenericApplicationContext applicationContext;

    public SpringContextHolderVerticle(GenericApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public GenericApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void start() {
        super.start();
        applicationContext.start();
        applicationContext.registerShutdownHook();
    }

    @Override
    public void stop() {
        super.stop();
        applicationContext.stop();
    }
}
