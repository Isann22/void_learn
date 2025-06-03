package com.example.voidlearn.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.voidlearn.model.Course;

public interface CourseRepository extends JpaRepository<Course,String>{

}


