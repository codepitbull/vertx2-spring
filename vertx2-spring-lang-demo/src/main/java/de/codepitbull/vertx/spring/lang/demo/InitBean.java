package de.codepitbull.vertx.spring.lang.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.vertx.java.platform.Container;


/**
 * @author Jochen Mader
 */
public class InitBean implements ApplicationListener<ContextStartedEvent> {

    @Autowired
    private Container container;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        container.deployVerticle("spring:httpVerticle");
    }
}
