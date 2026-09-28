package com.eventease.EventEase.service;

import com.eventease.EventEase.entity.Event;
import com.eventease.EventEase.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event addEvent(Event event) {
        return repository.save(event);
    }

    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    public Event getEventById(Long id) {
        return repository.findById(id).orElse(null);
    }
}