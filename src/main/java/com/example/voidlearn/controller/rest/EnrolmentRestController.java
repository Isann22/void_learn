package com.example.voidlearn.controller.rest;

import com.example.voidlearn.dao.EnrolmentProjection;
import com.example.voidlearn.dto.EnrolmentDto;
import com.example.voidlearn.service.EnrolmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
@RequiredArgsConstructor
public class EnrolmentRestController {
    private final EnrolmentService enrolmentService;

    @GetMapping()
    public List<EnrolmentProjection> getEnrollmentData(){
        return enrolmentService.getEnrollmentData();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody EnrolmentDto request, @PathVariable String id) {
        try {
            enrolmentService.updateEnrolment(request,id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            enrolmentService.deleteEnroll(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

}
