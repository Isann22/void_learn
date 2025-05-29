package com.example.voidlearn.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller

public class HomeController {

    @GetMapping("/")
    public String hello(){
        return "index";
    }

    @GetMapping("/access-denied")
    public  String denied(){
        return "error/acces-denied";
    }
}
