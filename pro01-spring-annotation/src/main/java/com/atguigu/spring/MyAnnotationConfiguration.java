package com.atguigu.spring;

import com.atguigu.entity.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan()
public class MyAnnotationConfiguration {

    @Bean
    public Student getStudent() {
        return new Student();
    }

}
