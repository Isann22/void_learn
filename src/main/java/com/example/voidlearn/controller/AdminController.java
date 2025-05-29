package com.example.voidlearn.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String layout(Model model, HttpServletRequest request) {
        model.addAttribute("title", request.getSession().getAttribute("title"));
        model.addAttribute("view", request.getSession().getAttribute("view"));
        model.addAttribute("viewHTML", request.getSession().getAttribute("viewHTML"));

        return "layout/appLayout";
    }
        @GetMapping("admin/courses")
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

    @GetMapping("admin/users")
    public String adminUsers(Model model,
                               HttpServletRequest request,
                               Authentication authentication) {


        if (!hasAdminRole(authentication)) {
            return "redirect:/access-denied";
        }


        model.addAttribute("title", "Kelola pengguna - Admin Panel");
        model.addAttribute("view", "admin/users");
        model.addAttribute("viewHTML", "admin-users");

        return "layout/appLayout";
    }

        private boolean hasAdminRole(Authentication authentication) {
            return authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority ->
                            grantedAuthority.getAuthority().equals("ADMIN"));
        }
    }

