package com.eventease.EventEase.service;

import com.eventease.EventEase.entity.Student;
import com.eventease.EventEase.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository repository;
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }
    public Student addStudent(Student student) {
        return repository.save(student);
    }
    public List<Student> getAllStudents() {
        return repository.findAll();
    }
    public Student getStudentById(Long id) {
        return repository.findById(id).orElse(null);
    }
}