package com.example.voidlearn.controller;

import com.example.voidlearn.service.CourseService;
import com.example.voidlearn.service.EnrolmentService;
import com.example.voidlearn.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final CourseService courseService;
    private final UserService userService;
    private final EnrolmentService enrolmentService;


    @GetMapping("/dashboard")
    public String layout(Model model, HttpServletRequest request) {
        model.addAttribute("totalCourse",courseService.getTotalCourse());
        model.addAttribute("totalUser",userService.countRoleUser());
        model.addAttribute("totalEnroll",enrolmentService.getTotalEnrolment());
        model.addAttribute("title", request.getSession().getAttribute("title"));
        model.addAttribute("view", request.getSession().getAttribute("view"));
        model.addAttribute("viewHTML", request.getSession().getAttribute("viewHTML"));

        return "layout/appLayout";
    }

    @GetMapping("/courses")
        public String adminCourses(Model model,
                                   Authentication authentication) {


            if (!hasAdminRole(authentication)) {
                return "redirect:/access-denied";
            }

            model.addAttribute("scripts", "/js/courseData.js");
            model.addAttribute("title", "Kelola Kursus - Admin Panel");
            model.addAttribute("view", "admin/courses");
            model.addAttribute("viewHTML", "admin-courses");

            return "layout/appLayout";
        }

    @GetMapping("/enrollment")
    public String adminEnroll(Model model) {
        model.addAttribute("users",userService.getUserData());
        model.addAttribute("courses",courseService.getCourseData());
        model.addAttribute("scripts", "/js/enrollData.js");
        model.addAttribute("title", "Kelola Enrollment - Admin Panel");
        model.addAttribute("view", "admin/enrollments");
        model.addAttribute("viewHTML", "admin-enrolments");
        return "layout/appLayout";
    }

    @GetMapping("/users")
    public String adminUsers(Model model, Authentication authentication) {
        if (!hasAdminRole(authentication)) {
            return "redirect:/access-denied";
        }

        model.addAttribute("scripts",
                "/js/userData.js");

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

