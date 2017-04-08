package com.wolfbeacon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@ComponentScan(basePackages = {"com.wolfbeacon"})
@EnableAutoConfiguration
@PropertySources({
        @PropertySource("classpath:application.properties")
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
