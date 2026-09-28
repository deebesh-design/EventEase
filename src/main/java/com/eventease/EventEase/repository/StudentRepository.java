package com.eventease.EventEase.repository;

import com.eventease.EventEase.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}