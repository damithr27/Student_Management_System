package com.example.studentmanagement.service;

import com.example.studentmanagement.model.*;
import com.example.studentmanagement.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private MarksRepository marksRepo;

    @Autowired
    private ReportRepository reportRepo;

    // 🔐 Teacher Login (Only Section Head Teachers)
    public Teacher login(String username, String password) {
        // Hardcoded section head credentials
        if ("sectionhead".equals(username) && "admin123".equals(password)) {
            Teacher head = new Teacher();
            head.setTid(0L);
            head.settName("Admin Teacher");
            head.setSubject("Section Head Teacher");
            head.setClassName("All Classes");
            head.setUsername("sectionhead");
            head.setPassword("admin123");
            head.setTphoto("default.jpg");
            return head;
        }
        throw new RuntimeException("Invalid username or password or not authorized");
    }

    // 📊 Dashboard
    public Map<String, Object> getTeacherDashboard(Long tid) {
        Map<String, Object> data = new HashMap<>();
        teacherRepo.findById(tid).ifPresent(teacher -> data.put("teacher", teacher));
        data.put("students", studentRepo.findAll());
        return data;
    }

    // 🧑‍🎓 Add Student
    public Student addStudent(Student student) {
        return studentRepo.save(student);
    }

    // ✏️ Update Student
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

    // ❌ Delete Student
    public boolean deleteStudent(String sid) {
        if (studentRepo.existsById(sid)) {
            studentRepo.deleteById(sid);
            return true;
        }
        return false;
    }

    // ✅ Save Marks
    public Marks saveMarks(Marks marks) {
        String sid = marks.getStudent().getSid();
        Student student = studentRepo.findById(sid)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + sid));
        marks.setStudent(student);
        return marksRepo.save(marks);
    }

    // ✏️ Update Marks
    public Marks updateMarks(Long id, Marks updatedMarks) {
        String sid = updatedMarks.getStudent().getSid();
        Student student = studentRepo.findById(sid)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + sid));

        return marksRepo.findById(id).map(existingMarks -> {
            existingMarks.setMaths(updatedMarks.getMaths());
            existingMarks.setPhysics(updatedMarks.getPhysics());
            existingMarks.setChemestry(updatedMarks.getChemestry());
            existingMarks.setStudent(student);
            return marksRepo.save(existingMarks);
        }).orElseThrow(() -> new RuntimeException("Marks with ID " + id + " not found."));
    }

    // ❌ Delete Marks
    public boolean deleteMarks(Long id) {
        if (marksRepo.existsById(id)) {
            marksRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ Save Report
    public Report saveReport(Report report) {
        String sid = report.getStudent().getSid();
        Student student = studentRepo.findById(sid)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + sid));
        report.setStudent(student);
        return reportRepo.save(report);
    }

    // ✏️ Update Report
    public Report updateReport(Long id, Report updatedReport) {
        String sid = updatedReport.getStudent().getSid();
        Student student = studentRepo.findById(sid)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + sid));

        return reportRepo.findById(id).map(existingReport -> {
            existingReport.setStudent(student);
            existingReport.setDescription(updatedReport.getDescription());
            return reportRepo.save(existingReport);
        }).orElseThrow(() -> new RuntimeException("Report with ID " + id + " not found."));
    }

    // ❌ Delete Report
    public boolean deleteReport(Long id) {
        if (reportRepo.existsById(id)) {
            reportRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ Add Teacher
    public Teacher addTeacher(Teacher teacher) {
        return teacherRepo.save(teacher);
    }

    // ✏️ Update Teacher
    public Teacher updateTeacher(Teacher updated) {
        return teacherRepo.findById(updated.getTid()).map(existing -> {
            existing.settName(updated.gettName());
            existing.setSubject(updated.getSubject());
            existing.setClassName(updated.getClassName());
            existing.setUsername(updated.getUsername());
            existing.setPassword(updated.getPassword());
            existing.setTphoto(updated.getTphoto());
            return teacherRepo.save(existing);
        }).orElse(null);
    }

    // ❌ Delete Teacher
    public boolean deleteTeacher(Long tid) {
        if (teacherRepo.existsById(tid)) {
            teacherRepo.deleteById(tid);
            return true;
        }
        return false;
    }

    // 👤 Get Teacher
    public Teacher getTeacherById(Long tid) {
        return teacherRepo.findById(tid).orElse(null);
    }

    // 🔄 List Getters
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepo.findAll();
    }

    public List<Marks> getAllMarks() {
        return marksRepo.findAll();
    }

    public List<Report> getAllReports() {
        return reportRepo.findAll();
    }
}
