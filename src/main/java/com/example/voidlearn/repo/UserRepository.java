package com.example.voidlearn.repo;

import java.util.List;
import java.util.Optional;

import com.example.voidlearn.dao.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.voidlearn.model.User;


@Repository
public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'USER'")
    long countByRoleUser();


    @Query("SELECT u.id as id, u.name as name, u.username as username, u.email as email, u.role as role FROM User u")
    List<UserProjection> getUserData();
}
