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

    // CREATE
    public Event addEvent(Event event) {
        return repository.save(event);
    }

    // READ ALL
    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    // READ ONE
    public Event getEventById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE
    public Event updateEvent(Long id, Event event) {

        Event existingEvent = repository.findById(id).orElse(null);

        if (existingEvent == null) {
            return null;
        }

        existingEvent.setTitle(event.getTitle());
        existingEvent.setDate(event.getDate());
        existingEvent.setVenue(event.getVenue());
        existingEvent.setCapacity(event.getCapacity());
        existingEvent.setOrganizer(event.getOrganizer());

        return repository.save(existingEvent);
    }

    // DELETE
    public void deleteEvent(Long id) {
        repository.deleteById(id);
    }
}