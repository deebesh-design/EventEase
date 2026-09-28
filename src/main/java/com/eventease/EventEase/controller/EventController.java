package com.eventease.EventEase.controller;

import com.eventease.EventEase.dto.EventDTO;
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

    @PostMapping("/organizer/{organizerId}")
    public Event addEvent(
            @PathVariable Long organizerId,
            @RequestBody EventDTO dto) {

        Event event = new Event();

        event.setTitle(dto.getTitle());
        event.setDate(dto.getDate());
        event.setVenue(dto.getVenue());
        event.setCapacity(dto.getCapacity());

        return service.addEvent(organizerId, event);
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
    // GET EVENTS OF ORGANIZER
    // =====================================================

    @GetMapping("/organizer/{organizerId}")
    public List<Event> getEventsByOrganizer(
            @PathVariable Long organizerId) {

        return service.getEventsByOrganizer(organizerId);
    }

    // =====================================================
    // UPDATE EVENT
    // =====================================================

    @PutMapping("/{eventId}/organizer/{organizerId}")
    public Event updateEvent(
            @PathVariable Long eventId,
            @PathVariable Long organizerId,
            @RequestBody EventDTO dto) {

        Event event = new Event();

        event.setTitle(dto.getTitle());
        event.setDate(dto.getDate());
        event.setVenue(dto.getVenue());
        event.setCapacity(dto.getCapacity());

        return service.updateEvent(
                eventId,
                organizerId,
                event
        );
    }

    // =====================================================
    // DELETE EVENT
    // =====================================================

    @DeleteMapping("/{eventId}/organizer/{organizerId}")
    public String deleteEvent(
            @PathVariable Long eventId,
            @PathVariable Long organizerId) {

        service.deleteEvent(
                eventId,
                organizerId
        );

        return "Event deleted successfully";
    }

    // =====================================================
    // GET REGISTERED COUNT
    // =====================================================

    @GetMapping("/{eventId}/registered-count")
    public long getRegisteredCount(
            @PathVariable Long eventId) {

        return service.getRegisteredCount(eventId);
    }

    // =====================================================
    // GET AVAILABLE SEATS
    // =====================================================

    @GetMapping("/{eventId}/available-seats")
    public long getAvailableSeats(
            @PathVariable Long eventId) {

        return service.getAvailableSeats(eventId);
    }

    // =====================================================
    // GET EVENT STATUS
    // =====================================================

    @GetMapping("/{eventId}/status")
    public String getEventStatus(
            @PathVariable Long eventId) {

        return service.getEventStatus(eventId);
    }
}