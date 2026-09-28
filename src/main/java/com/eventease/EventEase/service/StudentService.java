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

    // CREATE
    public Student addStudent(Student student) {
        return repository.save(student);
    }

    // READ ALL
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    // READ ONE
    public Student getStudentById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE
    public Student updateStudent(Long id, Student student) {

        Student existingStudent = repository.findById(id).orElse(null);

        if (existingStudent == null) {
            return null;
        }

        existingStudent.setName(student.getName());
        existingStudent.setEmail(student.getEmail());

        return repository.save(existingStudent);
    }

    // DELETE
    public void deleteStudent(Long id) {
        repository.deleteById(id);
    }
}