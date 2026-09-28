package com.eventease.EventEase.repository;

import com.eventease.EventEase.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}