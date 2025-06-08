package com.example.voidlearn.repo;

import com.example.voidlearn.dao.EnrolmentProjection;
import com.example.voidlearn.model.Enrolment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment,String> {
    @Query("SELECT e.id as enrolmentId, u.name as userName, c.title as courseName,c.createdDate as createdDate " +
            "FROM Enrolment e " +
            "JOIN e.user u " +
            "JOIN e.course c")
    List<EnrolmentProjection> getEnrollmentData();

    boolean existsByUserIdAndCourseId(String userId,String courseId);
    List<Enrolment> findByUserId(String id);
}
