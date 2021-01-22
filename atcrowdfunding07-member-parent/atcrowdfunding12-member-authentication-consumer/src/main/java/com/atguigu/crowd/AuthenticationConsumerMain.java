package com.atguigu.crowd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AuthenticationConsumerMain {

    public static void main(String[] args) {

        SpringApplication.run(AuthenticationConsumerMain.class, args);
    }
}
