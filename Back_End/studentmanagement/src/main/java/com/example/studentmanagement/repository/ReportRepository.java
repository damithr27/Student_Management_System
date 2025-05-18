package com.example.studentmanagement.repository;

import com.example.studentmanagement.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStudent_Sid(String sid);
}


