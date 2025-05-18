package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findByUsername(String username);
}


