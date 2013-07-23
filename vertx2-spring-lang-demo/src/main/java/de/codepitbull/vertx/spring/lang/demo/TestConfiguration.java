package de.codepitbull.vertx.spring.lang.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jochen Mader
 */
@Configuration
public class TestConfiguration {

    @Bean
    public HttpVerticle httpVerticle() {
        return new HttpVerticle();
    }

    @Bean
    public InitBean initBean() {
        return new InitBean();
    }
}
