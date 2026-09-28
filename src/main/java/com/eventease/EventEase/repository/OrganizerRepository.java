package com.eventease.EventEase.repository;

import com.eventease.EventEase.entity.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizerRepository extends JpaRepository<Organizer, Long> {

    Organizer findByEmail(String email);
}