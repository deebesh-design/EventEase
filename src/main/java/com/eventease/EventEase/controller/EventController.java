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

    @PostMapping
    public Event addEvent(@RequestBody Event event) {
        return service.addEvent(event);
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return service.getAllEvents();
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return service.getEventById(id);
    }
}