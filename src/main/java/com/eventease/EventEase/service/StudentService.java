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

        System.out.println("Name: " + student.getName());
        System.out.println("Email: " + student.getEmail());
        System.out.println("Password: " + student.getPassword());

        if (student.getName() == null
                || student.getName().trim().isEmpty()) {

            throw new RuntimeException(
                    "Student name is required"
            );
        }

        if (student.getEmail() == null
                || student.getEmail().trim().isEmpty()) {

            throw new RuntimeException(
                    "Student email is required"
            );
        }

        if (student.getPassword() == null
                || student.getPassword().trim().isEmpty()) {

            throw new RuntimeException(
                    "Student password is required"
            );
        }

        return repository.save(student);
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student getStudentById(Long id) {

        Student student =
                repository.findById(id).orElse(null);

        if (student == null) {

            throw new RuntimeException(
                    "Student not found"
            );
        }

        return student;
    }

    public Student updateStudent(
            Long id,
            Student student) {

        Student existingStudent =
                repository.findById(id).orElse(null);

        if (existingStudent == null) {

            throw new RuntimeException(
                    "Student not found"
            );
        }

        if (student.getName() == null
                || student.getName().trim().isEmpty()) {

            throw new RuntimeException(
                    "Student name is required"
            );
        }

        if (student.getEmail() == null
                || student.getEmail().trim().isEmpty()) {

            throw new RuntimeException(
                    "Student email is required"
            );
        }

        existingStudent.setName(
                student.getName()
        );

        existingStudent.setEmail(
                student.getEmail()
        );

        if (student.getPassword() != null
                && !student.getPassword().trim().isEmpty()) {

            existingStudent.setPassword(
                    student.getPassword()
            );
        }

        return repository.save(existingStudent);
    }

    public void deleteStudent(Long id) {

        Student student =
                repository.findById(id).orElse(null);

        if (student == null) {

            throw new RuntimeException(
                    "Student not found"
            );
        }

        repository.delete(student);
    }

    public Student login(
            String email,
            String password) {

        Student student =
                repository.findByEmail(email);

        if (student == null) {

            throw new RuntimeException(
                    "Student not found"
            );
        }

        if (!student.getPassword().equals(password)) {

            throw new RuntimeException(
                    "Invalid password"
            );
        }

        return student;
    }
}