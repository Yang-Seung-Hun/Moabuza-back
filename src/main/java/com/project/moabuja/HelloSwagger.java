package com.project.moabuja;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloSwagger {

    @GetMapping("/hello")
    public String hello(){
        return "hi there";
    }

    @PostMapping("/hello")
    public String hello(@RequestBody Hello hello){
        return hello.getHello() + hello.getName();
    }

    @PostMapping("/hello-git")
    public String helloGit(@RequestBody Hello hello){
        return hello.getHello()+hello.getName();
    }

    @PostMapping("/hey-github")
    public String heyGithub(@RequestBody Hello hello) {
        return hello.getHello()+hello.getName();
    }

    @PostMapping("/hey-github2")
    public String heyGithub2(@RequestBody Hello hello) {
        return hello.getHello()+hello.getName();
    }

}
