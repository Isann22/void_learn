package com.example.voidlearn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "enrolments")
public class Enrolment extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @PrePersist
    protected void onCreate() {
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }
}
