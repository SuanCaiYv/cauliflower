package com.codewithbuff.cauliflower.secure;

import com.codewithbuff.cauliflower.secure.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@SpringBootApplication
@EnableEurekaClient
public class SecureApplication {

    @Autowired
    private UserHandler userHandler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions
                .route()
                .PUT("/login", userHandler::loginIn)
                .DELETE("/logout", userHandler::loginOut)
                .POST("/signin", userHandler::signIn)
                .DELETE("/signout", userHandler::signOut)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SecureApplication.class, args);
    }
}
