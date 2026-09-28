package com.eventease.EventEase.repository;

import com.eventease.EventEase.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository
        extends JpaRepository<Registration, Long> {

    List<Registration> findByEventId(Long eventId);

    long countByEventId(Long eventId);
}