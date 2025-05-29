package com.example.voidlearn.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    @GetMapping("/layout")
    public  String layout(Model model){
        model.addAttribute("title","user");
        model.addAttribute("view","user/dashboard");
        model.addAttribute("viewHTML","user-dashboard");
        return "layout/appLayout";
    }
}
