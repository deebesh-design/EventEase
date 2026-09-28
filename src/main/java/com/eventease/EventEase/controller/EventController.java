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

    // =====================================================
    // CREATE EVENT
    // =====================================================

    @PostMapping
    public Event addEvent(
            @RequestParam Long organizerId,
            @RequestBody Event event) {

        return service.addEvent(
                organizerId,
                event
        );
    }

    // =====================================================
    // GET ALL EVENTS
    // =====================================================

    @GetMapping
    public List<Event> getAllEvents() {

        return service.getAllEvents();
    }

    // =====================================================
    // GET ONE EVENT
    // =====================================================

    @GetMapping("/{id}")
    public Event getEventById(
            @PathVariable Long id) {

        return service.getEventById(id);
    }

    // =====================================================
    // GET ORGANIZER EVENTS
    // =====================================================

    @GetMapping("/organizer/{organizerId}")
    public List<Event> getEventsByOrganizer(
            @PathVariable Long organizerId) {

        return service.getEventsByOrganizer(
                organizerId
        );
    }

    // =====================================================
    // GET REGISTERED COUNT
    // =====================================================

    @GetMapping("/{id}/registered-count")
    public long getRegisteredCount(
            @PathVariable Long id) {

        return service.getRegisteredCount(id);
    }

    // =====================================================
    // GET AVAILABLE SEATS
    // =====================================================

    @GetMapping("/{id}/available-seats")
    public long getAvailableSeats(
            @PathVariable Long id) {

        return service.getAvailableSeats(id);
    }

    // =====================================================
    // GET EVENT STATUS
    // =====================================================

    @GetMapping("/{id}/status")
    public String getEventStatus(
            @PathVariable Long id) {

        return service.getEventStatus(id);
    }

    // =====================================================
    // UPDATE EVENT
    // =====================================================

    @PutMapping("/{id}")
    public Event updateEvent(
            @PathVariable Long id,
            @RequestParam Long organizerId,
            @RequestBody Event event) {

        return service.updateEvent(
                id,
                organizerId,
                event
        );
    }

    // =====================================================
    // DELETE EVENT
    // =====================================================

    @DeleteMapping("/{id}")
    public String deleteEvent(
            @PathVariable Long id,
            @RequestParam Long organizerId) {

        service.deleteEvent(
                id,
                organizerId
        );

        return "Event deleted successfully";
    }
}