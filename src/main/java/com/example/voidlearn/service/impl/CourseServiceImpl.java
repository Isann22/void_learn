package com.example.voidlearn.service.impl;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.voidlearn.dao.CourseProjection;
import com.example.voidlearn.repo.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.voidlearn.dto.CourseDto;
import com.example.voidlearn.model.Course;
import com.example.voidlearn.model.User;
import com.example.voidlearn.repo.CourseRepository;
import com.example.voidlearn.service.CourseService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    @Override
    public List<CourseProjection> getCourseData() {
        return courseRepository.getCourseData();
    }

    @Override
    public Course createCourse(CourseDto courseDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User currentUser)) {
            throw new SecurityException("Invalid user principal type");
        }

        if (currentUser.getId() == null) {
            throw new IllegalStateException("Authenticated user has null ID");
        }


        User persistentUser = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException(
                        "User not found with ID: " + currentUser.getId()));

        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(courseDto.getDescription());
        course.setImage(courseDto.getImage());
        course.setUser(persistentUser);
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> getById(String id) {
        return courseRepository.findById(id);
    }

    @Override
   public Course updateCourse(CourseDto courseDto,String id){
    Course course = courseRepository.findById(id).get();
    course.setTitle(courseDto.getTitle());
    course.setDescription(courseDto.getDescription());
    course.setImage(courseDto.getImage());
    course.setModifiedDate(LocalDateTime.now());
    return courseRepository.save(course);
   }

    @Override
    public Long getTotalCourse() {
        return courseRepository.count();
    }

    @Override
   public void deleteUser(String id){
    courseRepository.deleteById(id);
   }


}
