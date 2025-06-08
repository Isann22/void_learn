package com.example.voidlearn.dao;

import java.time.LocalDateTime;

public interface EnrolmentProjection {
    String getEnrolmentId();
    String getUserName();
    String getCourseName();
    LocalDateTime getCreatedDate();
}
