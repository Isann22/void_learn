package com.example.voidlearn.controller;

import com.example.voidlearn.dto.EnrolmentDto;
import com.example.voidlearn.model.Course;
import com.example.voidlearn.model.Enrolment;
import com.example.voidlearn.model.User;
import com.example.voidlearn.service.CourseService;
import com.example.voidlearn.service.EnrolmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EnrolmentContoller {

    private final CourseService courseService;
    private final EnrolmentService enrolmentService;

    @GetMapping("/courses/enroll/{id}")
    public String getDetailEnrolment(@PathVariable String id, Model model, @AuthenticationPrincipal User user) {
        Course course = courseService.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        model.addAttribute("course", course);
        model.addAttribute("user", user);
        model.addAttribute("request",new EnrolmentDto());
        model.addAttribute("scripts", "/js/enrolService.js");

        model.addAttribute("title", "Enroll in Course");
        model.addAttribute("view", "user/enrolment");
        model.addAttribute("viewHTML", "user-enrolments");

        return "layout/appLayout";
    }

    @PostMapping(path = "/course/enrollment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> enrolCourse(@RequestBody EnrolmentDto request) {
        try {
            EnrolmentDto enrolmentDto = new EnrolmentDto();
            enrolmentDto.setCourseId(request.getCourseId());
            enrolmentDto.setUserId(request.getUserId());
            enrolmentService.createEnrolment(enrolmentDto);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @GetMapping("/user/my-courses")
    public String getMyCourses(Model model,@AuthenticationPrincipal User user) {
        try {
            List<Enrolment> enrolments = enrolmentService.findByUserId(user.getId());
            model.addAttribute("enrolments", enrolments);
            model.addAttribute("title", "Enroll in Course");
            model.addAttribute("view", "user/my-courses");
            model.addAttribute("viewHTML", "user-myCourses");
            return "layout/appLayout";
        }catch (IllegalStateException er){
            model.addAttribute("error",er);
            return "error/500";
        }
    }
}
