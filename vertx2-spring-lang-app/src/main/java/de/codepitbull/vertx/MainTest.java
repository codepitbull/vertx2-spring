package de.codepitbull.vertx;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.platform.PlatformLocator;
import org.vertx.java.platform.PlatformManager;

import java.io.IOException;

/**
 * @author Jochen Mader
 */
public class MainTest {
    public static void main(String[] args) {

        System.setProperty("vertx.langs.spring", "de.codepitbull.vertx~spring-lang~1.0.0-final:de.codepitbull.vertx.spring.lang.SpringVerticleFactory");

        final PlatformManager pm = PlatformLocator.factory.createPlatformManager();

        pm.installModule("de.codepitbull.vertx~spring-lang~1.0.0-final", new Handler<AsyncResult<Void>>() {
            @Override
            public void handle(AsyncResult<Void> event) {
                if (event.cause() != null) {
                    System.out.println("Unable to deploy de.codepitbull~spring-lang~1.0.0-final");
                    event.cause().printStackTrace();
                }
                pm.deployModule("de.codepitbull~spring-lang-spring~1.0.0-final", null, 1, new Handler<AsyncResult<String>>() {
                    @Override
                    public void handle(AsyncResult<String> event) {
                        if (event.cause() != null) {
                            System.out.println("Unable to deploy de.codepitbull~spring-lang-spring~1.0.0-final");
                            event.cause().printStackTrace();
                        }
                    }
                });
            }
        });

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
