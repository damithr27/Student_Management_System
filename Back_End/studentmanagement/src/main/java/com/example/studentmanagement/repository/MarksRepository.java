package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Marks;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByStudent_Sid(String sid);
}
