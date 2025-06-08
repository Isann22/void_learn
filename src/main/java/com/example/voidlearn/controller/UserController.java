package com.example.voidlearn.controller;

import com.example.voidlearn.dto.UserDto;


import com.example.voidlearn.model.Course;
import com.example.voidlearn.model.User;
import com.example.voidlearn.service.CourseService;
import com.example.voidlearn.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CourseService courseService;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/register";
    }


    @PostMapping("/register")
    public String procesRegister(@ModelAttribute("user")
                                     @Valid UserDto request,
                                 Model model)
    {
        try {

            userService.register(request);
            return "redirect:auth/login?success=Registrasi+berhasil%21";
        }catch (RuntimeException err){
            model.addAttribute("error",err.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/user/dashboard")
    public String layout(Model model, HttpServletRequest request) {
        model.addAttribute("title", request.getSession().getAttribute("title"));
        model.addAttribute("view", request.getSession().getAttribute("view"));
        model.addAttribute("viewHTML", request.getSession().getAttribute("viewHTML"));


        return "layout/appLayout";
    }

    @GetMapping("/user/courses")
    public String userCourses(Model model) {

        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        model.addAttribute("title", "Kelola Kursus - Admin Panel");
        model.addAttribute("view", "user/courses");
        model.addAttribute("viewHTML", "user-courses");

        return "layout/appLayout";
    }

    @GetMapping("/user/profile")
    public String userProfile(Model model,
                              @AuthenticationPrincipal User user) {

        model.addAttribute("email", user.getEmail());
        model.addAttribute("username", user.getUsername());

        model.addAttribute("title", "User Profile - My Application");
        model.addAttribute("view", "user/user-profile");
        model.addAttribute("viewHTML", "user-profile");

        return "layout/appLayout";
    }

    @PostMapping("/profile/delete")
     public String deleteUserAccount(
                @AuthenticationPrincipal User user,
                @RequestParam("currentPassword") String currentPassword,
                RedirectAttributes redirectAttributes,
                HttpServletRequest request) {

            try {

                if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                    redirectAttributes.addFlashAttribute("error", "Incorrect current password");
                    return "redirect:/user/profile";
                }

                userService.deleteUser(user.getId());

                new SecurityContextLogoutHandler().logout(request, null, null);

                redirectAttributes.addFlashAttribute("success", "Your account has been deleted successfully");
                return "redirect:/login?accountDeleted";

            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Failed to delete account: " + e.getMessage());
                return "redirect:/user/profile";
            }
        }
    }

