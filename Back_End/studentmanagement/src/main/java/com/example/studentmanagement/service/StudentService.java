package com.example.studentmanagement.service;

import com.example.studentmanagement.model.*;
import com.example.studentmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private MarksRepository marksRepo;

    @Autowired
    private ReportRepository reportRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    // 🔐 Student Login
    public Student login(String username, String password) {
        return studentRepo.findByUsername(username)
                .filter(s -> s.getPassword().equals(password))
                .orElse(null);
    }

    // 📊 Dashboard (Student + Marks + Report + Teachers)
    public Map<String, Object> getStudentDashboard(String sid) {
        Map<String, Object> data = new HashMap<>();

        studentRepo.findById(sid).ifPresent(student -> data.put("student", student));
        marksRepo.findByStudent_Sid(sid).stream().findFirst().ifPresent(marks -> data.put("marks", marks));
        reportRepo.findByStudent_Sid(sid).stream().findFirst().ifPresent(report -> data.put("report", report));
        data.put("teachers", teacherRepo.findAll());

        return data;
    }

    // 🔍 Get student by ID
    public Student getStudentById(String sid) {
        return studentRepo.findById(sid).orElse(null);
    }

    // 🧑‍🎓 Register/Add student
    public Student addStudent(Student student) {
        return studentRepo.save(student);
    }

    // ✏️ Update student
    public Student updateStudent(String sid, Student updated) {
        return studentRepo.findById(sid).map(existing -> {
            existing.setSName(updated.getSName());
            existing.setAddress(updated.getAddress());
            existing.setDob(updated.getDob());
            existing.setStream(updated.getStream());
            existing.setParents(updated.getParents());
            existing.setAttendance(updated.getAttendance());
            existing.setUsername(updated.getUsername());
            existing.setPassword(updated.getPassword());
            existing.setSphoto(updated.getSphoto());
            return studentRepo.save(existing);
        }).orElse(null);
    }

    // ❌ Delete student
    public boolean deleteStudent(String sid) {
        if (studentRepo.existsById(sid)) {
            studentRepo.deleteById(sid);
            return true;
        }
        return false;
    }
}
