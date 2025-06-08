package com.example.voidlearn.repo;

import com.example.voidlearn.dao.CourseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.voidlearn.model.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,String>{
    @Query("SELECT c.id as id, c.title as title, c.description as description, c.image as image FROM Course c")
    List<CourseProjection> getCourseData();
}


