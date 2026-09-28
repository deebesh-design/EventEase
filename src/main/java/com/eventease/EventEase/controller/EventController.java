package com.eventease.EventEase.controller;

import com.eventease.EventEase.entity.Event;
import com.eventease.EventEase.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public Event addEvent(@RequestBody Event event) {
        return service.addEvent(event);
    }

    // READ ALL
    @GetMapping
    public List<Event> getAllEvents() {
        return service.getAllEvents();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return service.getEventById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Event updateEvent(
            @PathVariable Long id,
            @RequestBody Event event) {

        return service.updateEvent(id, event);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable Long id) {

        service.deleteEvent(id);

        return "Event deleted successfully";
    }
}