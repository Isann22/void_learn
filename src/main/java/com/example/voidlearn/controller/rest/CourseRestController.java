package com.example.voidlearn.controller.rest;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.voidlearn.model.Course;
import com.example.voidlearn.service.CourseService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.voidlearn.dto.CourseDto;



@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseRestController {

    private final CourseService courseService;

    @GetMapping()
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody CourseDto course) {
        courseService.createCourse(course);
        return ResponseEntity.ok().build();
    }
    
    

}
