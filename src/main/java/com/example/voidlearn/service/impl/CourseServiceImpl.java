package com.example.voidlearn.service.impl;

import java.util.List;

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

    @Override
    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    @Override
    public Course createCourse(CourseDto courseDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Course course = new Course();
        course.setTitle(courseDto.getTitle());
        course.setDescription(course.getDescription());
        course.setImage(course.getImage());
        User userid = new User();
        userid.setId(user.getId());
        course.setUser(userid);
        return courseRepository.save(course);
    }


   @Override
   public Course updateCourse(CourseDto courseDto,String id){
    Course course = courseRepository.findById(id).get();
    course.setTitle(courseDto.getTitle());
    course.setDescription(course.getDescription());
    course.setImage(course.getImage());
    return courseRepository.save(course);
   }
   
   @Override
   public void deleteUser(String id){
    courseRepository.deleteById(id);
   }

}
