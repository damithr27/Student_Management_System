package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 🔐 Student Login with username and password
    @PostMapping("/login")
    public Student login(@RequestParam String username, @RequestParam String password) {
        return studentService.login(username, password);
    }

    // 📊 Student Dashboard (student + marks + report + teachers)
    @GetMapping("/dashboard/{sid}")
    public Map<String, Object> getDashboard(@PathVariable String sid) {
        return studentService.getStudentDashboard(sid);
    }

    // 🔍 Get a student's details by ID
    @GetMapping("/{sid}")
    public Student getStudent(@PathVariable String sid) {
        return studentService.getStudentById(sid);
    }

    // 📝 Update student profile (if allowed)
    @PutMapping("/{sid}")
    public ResponseEntity<Student> updateStudent(@PathVariable String sid, @RequestBody Student student) {
        Student updated = studentService.updateStudent(sid, student);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    // ❌ Delete student (optional)
    @DeleteMapping("/{sid}")
    public ResponseEntity<?> deleteStudent(@PathVariable String sid) {
        if (studentService.deleteStudent(sid)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 🧑‍🎓 Register a new student (if not handled by admin/teacher)
    @PostMapping("/register")
    public Student registerStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }
}
