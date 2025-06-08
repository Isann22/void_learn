package com.example.voidlearn.service.impl;


import com.example.voidlearn.dao.EnrolmentProjection;
import com.example.voidlearn.dto.EnrolmentDto;
import com.example.voidlearn.model.Course;
import com.example.voidlearn.model.Enrolment;
import com.example.voidlearn.model.User;
import com.example.voidlearn.repo.CourseRepository;
import com.example.voidlearn.repo.EnrolmentRepository;
import com.example.voidlearn.repo.UserRepository;
import com.example.voidlearn.service.EnrolmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrolmentServiceImpl implements EnrolmentService {

    private final EnrolmentRepository enrolmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public Enrolment createEnrolment(EnrolmentDto enrolmentDto) {

        if (enrolmentRepository.existsByUserIdAndCourseId(
                enrolmentDto.getUserId(),
                enrolmentDto.getCourseId())) {
            throw new IllegalStateException("Anda sudah daftar di course ini");
        }

        User user = userRepository.findById(enrolmentDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Course course = courseRepository.findById(enrolmentDto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Enrolment enrolment = new Enrolment();
        enrolment.setUser(user);
        enrolment.setCourse(course);
        return enrolmentRepository.save(enrolment);
    }

    @Override
    public List<EnrolmentProjection> getEnrollmentData() {
        return enrolmentRepository.getEnrollmentData();
    }

    @Override
    public void deleteEnroll(String id) {
        enrolmentRepository.deleteById(id);
    }

    @Override
    public Enrolment updateEnrolment(EnrolmentDto enrolmentDto,String enrolId) {
        Enrolment enrolment = enrolmentRepository.findById(enrolId).
                orElseThrow(() -> new EntityNotFoundException("Course not found"));
        User user = userRepository.findById(enrolmentDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Course course = courseRepository.findById(enrolmentDto.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        enrolment.setUser(user);
        enrolment.setCourse(course);
        return enrolmentRepository.save(enrolment);
    }

    @Override
    public List<Enrolment> findByUserId(String id) {
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

        return enrolmentRepository.findByUserId(currentUser.getId());
    }

    @Override
    public Long getTotalEnrolment() {
        return enrolmentRepository.count();
    }


}
