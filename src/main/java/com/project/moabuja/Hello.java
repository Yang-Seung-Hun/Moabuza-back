package com.project.moabuja;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    private String hello;
    private String name;

    public Hello() {
    }

    public Hello(String hello, String name) {
        this.hello = hello;
        this.name = name;
    }

    public String getHello() {
        return hello;
    }

    public String getName() {
        return name;
    }


    @GetMapping("/health")
    public String healthCheck(){
        return "건강";
    }
}
