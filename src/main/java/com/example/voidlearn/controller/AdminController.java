package com.example.voidlearn.controller;

import com.example.voidlearn.dto.CreateUserAdminRequest;
import com.example.voidlearn.model.User;
import com.example.voidlearn.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    @GetMapping("/dashboard")
    public String layout(Model model, HttpServletRequest request) {
        model.addAttribute("title", request.getSession().getAttribute("title"));
        model.addAttribute("view", request.getSession().getAttribute("view"));
        model.addAttribute("viewHTML", request.getSession().getAttribute("viewHTML"));

        return "layout/appLayout";
    }

    @GetMapping("/courses")
        public String adminCourses(Model model,
                                   HttpServletRequest request,
                                   Authentication authentication) {


            if (!hasAdminRole(authentication)) {
                return "redirect:/access-denied";
            }


            model.addAttribute("title", "Kelola Kursus - Admin Panel");
            model.addAttribute("view", "admin/courses");
            model.addAttribute("viewHTML", "admin-courses");

            return "layout/appLayout";
        }

    @GetMapping("/users")
    public String adminUsers(Model model,
                               HttpServletRequest request,
                               Authentication authentication) {


        if (!hasAdminRole(authentication)) {
            return "redirect:/access-denied";
        }

        model.addAttribute("title", "Kelola pengguna - Admin Panel");
        model.addAttribute("view", "admin/users");
        model.addAttribute("viewHTML", "admin-users");
        model.addAttribute("newUser", new CreateUserAdminRequest());
        return "layout/appLayout";
    }



        private boolean hasAdminRole(Authentication authentication) {
            return authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority ->
                            grantedAuthority.getAuthority().equals("ADMIN"));
        }
    }

