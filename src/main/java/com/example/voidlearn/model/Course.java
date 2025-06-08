package com.example.voidlearn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "courses")
public class Course extends BaseEntity {

    @Column(nullable = false)
    private String title;

    private String description;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrolment> enrolments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
    }
}
