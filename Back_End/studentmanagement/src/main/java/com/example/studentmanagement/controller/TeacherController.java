package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.*;
import com.example.studentmanagement.repository.MarksRepository;
import com.example.studentmanagement.repository.ReportRepository;
import com.example.studentmanagement.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@CrossOrigin
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private MarksRepository marksRepo;

    @Autowired
    private ReportRepository reportRepo;

    private boolean isSectionHead(Teacher teacher) {
        return "Section Head Teacher".equalsIgnoreCase(teacher.getSubject());
    }

    // 👨‍🏫 Teacher Login
    @PostMapping("/login")
    public Teacher login(@RequestParam String username, @RequestParam String password) {
        return teacherService.login(username, password);
    }

    // 📊 Teacher Dashboard
    @GetMapping("/dashboard/{tid}")
    public ResponseEntity<?> getDashboard(@PathVariable Long tid) {
        Teacher teacher = teacherService.getTeacherById(tid);
        if (teacher != null && isSectionHead(teacher)) {
            return ResponseEntity.ok(teacherService.getTeacherDashboard(tid));
        }
        return ResponseEntity.status(403).body("Access denied: Only Section Head Teachers can access the dashboard.");
    }

    // 🧑‍🎓 Add Student
    @PostMapping("/student")
    public ResponseEntity<?> addStudent(@RequestBody Student student, @RequestParam Long tid) {
        Teacher teacher = teacherService.getTeacherById(tid);
        if (teacher != null && isSectionHead(teacher)) {
            return ResponseEntity.ok(teacherService.addStudent(student));
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    // ✏️ Update Student
    @PutMapping("/student/{sid}")
    public ResponseEntity<?> updateStudent(@PathVariable String sid, @RequestBody Student student, @RequestParam Long tid) {
        Teacher teacher = teacherService.getTeacherById(tid);
        if (teacher != null && isSectionHead(teacher)) {
            return ResponseEntity.ok(teacherService.updateStudent(sid, student));
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    // ❌ Delete Student
    @DeleteMapping("/student/{sid}")
    public ResponseEntity<?> deleteStudent(@PathVariable String sid, @RequestParam Long tid) {
        Teacher teacher = teacherService.getTeacherById(tid);
        if (teacher != null && isSectionHead(teacher)) {
            return teacherService.deleteStudent(sid)
                    ? ResponseEntity.ok("Student deleted successfully")
                    : ResponseEntity.status(404).body("Student not found");
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    // ➕ Add Marks
    @PostMapping("/marks")
    public ResponseEntity<?> saveMarks(@RequestBody Marks marks, @RequestParam Long tid) {
        Teacher teacher = teacherService.getTeacherById(tid);
        if (teacher != null && isSectionHead(teacher)) {
            return ResponseEntity.ok(teacherService.saveMarks(marks));
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    // ✏️ Update Marks
    @PutMapping("/marks/{id}")
    public ResponseEntity<?> updateMarks(@PathVariable Long id, @RequestBody Marks updatedMarks, @RequestParam Long tid) {
        Teacher teacher = teacherService.getTeacherById(tid);
        if (teacher != null && isSectionHead(teacher)) {
            return ResponseEntity.ok(teacherService.updateMarks(id, updatedMarks));
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    // 🔍 Get Marks by ID
    @GetMapping("/marks/{id}")
    public ResponseEntity<Marks> getMarksById(@PathVariable Long id) {
        return marksRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ❌ Delete Marks
    @DeleteMapping("/marks/{id}")
    public ResponseEntity<?> deleteMarks(@PathVariable Long id, @RequestParam Long tid) {
        Teacher teacher = teacherService.getTeacherById(tid);
        if (teacher != null && isSectionHead(teacher)) {
            if (marksRepo.existsById(id)) {
                marksRepo.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    // ➕ Add Report
    @PostMapping("/report")
    public ResponseEntity<?> saveReport(@RequestBody Report report, @RequestParam Long tid) {
        Teacher teacher = teacherService.getTeacherById(tid);
        if (teacher != null && isSectionHead(teacher)) {
            return ResponseEntity.ok(teacherService.saveReport(report));
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    // ✏️ Update Report
    @PutMapping("/report/{id}")
    public ResponseEntity<?> updateReport(@PathVariable Long id, @RequestBody Report updatedReport, @RequestParam Long tid) {
        Teacher teacher = teacherService.getTeacherById(tid);
        if (teacher != null && isSectionHead(teacher)) {
            return ResponseEntity.ok(teacherService.updateReport(id, updatedReport));
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    // 🔍 Get Report by ID
    @GetMapping("/report/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        return reportRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ❌ Delete Report
    @DeleteMapping("/report/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable Long id, @RequestParam Long tid) {
        Teacher teacher = teacherService.getTeacherById(tid);
        if (teacher != null && isSectionHead(teacher)) {
            if (reportRepo.existsById(id)) {
                reportRepo.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    // 🧑‍🏫 Add Teacher
    @PostMapping("/teacher")
    public ResponseEntity<?> addTeacher(@RequestBody Teacher teacher, @RequestParam Long tid) {
        Teacher requester = teacherService.getTeacherById(tid);
        if (requester != null && isSectionHead(requester)) {
            return ResponseEntity.ok(teacherService.addTeacher(teacher));
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    // ✏️ Update Teacher
    @PutMapping("/teacher/{tid}")
    public ResponseEntity<?> updateTeacher(@PathVariable Long tid, @RequestBody Teacher teacher, @RequestParam Long requesterId) {
        Teacher requester = teacherService.getTeacherById(requesterId);
        if (requester != null && isSectionHead(requester)) {
            teacher.setTid(tid);
            return ResponseEntity.ok(teacherService.updateTeacher(teacher));
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    // ❌ Delete Teacher
    @DeleteMapping("/{tid}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long tid, @RequestParam Long requesterId) {
        Teacher requester = teacherService.getTeacherById(requesterId);

        // Prevent deleting the section head who is not in the DB
        if (tid == 0) {
            return ResponseEntity.status(403).body("❌ Cannot delete the default Section Head Teacher.");
        }

        if (requester != null && "Section Head Teacher".equalsIgnoreCase(requester.getSubject())) {
            return teacherService.deleteTeacher(tid)
                    ? ResponseEntity.ok("✅ Teacher deleted successfully.")
                    : ResponseEntity.status(404).body("❌ Teacher not found.");
        }
        return ResponseEntity.status(403).body("❌ Access denied.");
    }

    // 📚 Get All Students
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return teacherService.getAllStudents();
    }

    // 🧑‍🏫 Get All Teachers
    @GetMapping("/teachers")
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    // 📝 Get All Marks
    @GetMapping("/marks")
    public List<Marks> getAllMarks() {
        return teacherService.getAllMarks();
    }

    // 📄 Get All Reports
    @GetMapping("/reports")
    public List<Report> getAllReports() {
        return teacherService.getAllReports();
    }

    // 👤 Get Teacher Details by ID
    @GetMapping("/{tid}")
    public Teacher getTeacher(@PathVariable Long tid) {
        return teacherService.getTeacherById(tid);
    }
}