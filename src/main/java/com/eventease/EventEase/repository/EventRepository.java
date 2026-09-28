package com.eventease.EventEase.repository;

import com.eventease.EventEase.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository
        extends JpaRepository<Event, Long> {

    List<Event> findByOrganizerId(Long organizerId);
}