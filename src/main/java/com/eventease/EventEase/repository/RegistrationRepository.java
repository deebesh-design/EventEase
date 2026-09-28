package com.eventease.EventEase.repository;

import com.eventease.EventEase.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
}