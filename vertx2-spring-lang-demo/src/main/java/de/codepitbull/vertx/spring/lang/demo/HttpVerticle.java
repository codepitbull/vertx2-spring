package de.codepitbull.vertx.spring.lang.demo;

import de.codepitbull.vertx.spring.lang.SpringVerticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.platform.Container;
import org.vertx.java.platform.Verticle;

import java.util.Map;

/**
 * @author Jochen Mader
 */
public class HttpVerticle extends SpringVerticle {

    @Override
    public void start() {
        vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> header : req.headers().entries()) {
                    sb.append(header.getKey()).append(": ").append(header.getValue()).append("\n");
                }
                req.response().putHeader("content-type", "text/plain");
                req.response().end(sb.toString());
            }
        }).listen(8071);
    }
}
