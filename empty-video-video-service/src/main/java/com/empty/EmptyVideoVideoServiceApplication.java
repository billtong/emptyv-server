package com.empty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class EmptyVideoVideoServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmptyVideoVideoServiceApplication.class, args);
    }
}

@Configuration
class RouterFunctionConfig {

}