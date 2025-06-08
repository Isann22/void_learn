package com.example.voidlearn.service;

import com.example.voidlearn.dao.EnrolmentProjection;
import com.example.voidlearn.dto.EnrolmentDto;
import com.example.voidlearn.model.Enrolment;

import java.util.List;

public interface EnrolmentService {
    Enrolment createEnrolment(EnrolmentDto enrolmentDto);
    List<EnrolmentProjection>getEnrollmentData();
    void deleteEnroll(String id);
    Enrolment updateEnrolment(EnrolmentDto enrolmentDto,String enrolId);
    List<Enrolment> findByUserId(String id);
    Long getTotalEnrolment();
}
