package com.example.voidlearn.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.voidlearn.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course,String>{

}


