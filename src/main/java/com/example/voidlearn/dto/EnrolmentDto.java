package com.example.voidlearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrolmentDto {
    private String enrolmentId;
    private String userId;
    private String courseId;
}
