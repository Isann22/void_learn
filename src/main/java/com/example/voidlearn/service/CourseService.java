package com.example.voidlearn.service;

import java.util.List;

import com.example.voidlearn.dto.CourseDto;
import com.example.voidlearn.model.Course;


public interface CourseService {
   List<Course> getAllCourses();
   Course createCourse(CourseDto courseDto);
   Course updateCourse(CourseDto courseDto,String id);
   void deleteUser(String id);
}