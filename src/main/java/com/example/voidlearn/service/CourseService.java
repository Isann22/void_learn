package com.example.voidlearn.service;

import java.util.List;
import java.util.Optional;

import com.example.voidlearn.dao.CourseProjection;
import com.example.voidlearn.dto.CourseDto;
import com.example.voidlearn.model.Course;



public interface CourseService {
   List<Course> getAllCourses();
   Course createCourse(CourseDto courseDto);
   Course updateCourse(CourseDto courseDto,String id);
   Optional<Course> getById(String id);
   void deleteUser(String id);
   Long getTotalCourse();
   List<CourseProjection> getCourseData();
}