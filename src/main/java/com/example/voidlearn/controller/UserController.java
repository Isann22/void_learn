package com.example.voidlearn.controller;

import com.example.voidlearn.dto.UserRegisterRequest;

import com.example.voidlearn.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegisterRequest());
        return "auth/register";
    }


    @PostMapping("/register")
    public String procesRegister(@ModelAttribute("user")
                                     @Valid UserRegisterRequest userRegisterRequest
                                 )
    {

        userService.register(userRegisterRequest);
        return "redirect:auth/login?success=Registrasi+berhasil%21";
    }

    @GetMapping("/user/dashboard")
    public String layout(Model model, HttpServletRequest request) {
        model.addAttribute("title", request.getSession().getAttribute("title"));
        model.addAttribute("view", request.getSession().getAttribute("view"));
        model.addAttribute("viewHTML", request.getSession().getAttribute("viewHTML"));


        return "layout/appLayout";
    }
}
