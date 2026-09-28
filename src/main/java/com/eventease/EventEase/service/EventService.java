package com.eventease.EventEase.service;

import com.eventease.EventEase.entity.Event;
import com.eventease.EventEase.entity.Organizer;
import com.eventease.EventEase.entity.Registration;
import com.eventease.EventEase.repository.EventRepository;
import com.eventease.EventEase.repository.OrganizerRepository;
import com.eventease.EventEase.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final OrganizerRepository organizerRepository;
    private final RegistrationRepository registrationRepository;

    public EventService(
            EventRepository eventRepository,
            OrganizerRepository organizerRepository,
            RegistrationRepository registrationRepository) {

        this.eventRepository = eventRepository;
        this.organizerRepository = organizerRepository;
        this.registrationRepository = registrationRepository;
    }

    // =====================================================
    // CREATE EVENT
    // =====================================================

    public Event addEvent(Long organizerId, Event event) {

        Organizer organizer =
                organizerRepository.findById(organizerId).orElse(null);

        if (organizer == null) {
            throw new RuntimeException("Organizer not found");
        }

        validateEvent(event);

        event.setOrganizer(organizer);

        return eventRepository.save(event);
    }

    // =====================================================
    // GET ALL EVENTS
    // =====================================================

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // =====================================================
    // GET ONE EVENT
    // =====================================================

    public Event getEventById(Long id) {

        Event event =
                eventRepository.findById(id).orElse(null);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        return event;
    }

    // =====================================================
    // GET EVENTS OF ORGANIZER
    // =====================================================

    public List<Event> getEventsByOrganizer(Long organizerId) {

        Organizer organizer =
                organizerRepository.findById(organizerId).orElse(null);

        if (organizer == null) {
            throw new RuntimeException("Organizer not found");
        }

        return eventRepository.findByOrganizerId(organizerId);
    }

    // =====================================================
    // UPDATE EVENT
    // =====================================================

    public Event updateEvent(
            Long eventId,
            Long organizerId,
            Event event) {

        Event existingEvent =
                eventRepository.findById(eventId).orElse(null);

        if (existingEvent == null) {
            throw new RuntimeException("Event not found");
        }

        Organizer organizer =
                organizerRepository.findById(organizerId).orElse(null);

        if (organizer == null) {
            throw new RuntimeException("Organizer not found");
        }

        // Check ownership
        if (!existingEvent.getOrganizer().getId().equals(organizerId)) {

            throw new RuntimeException(
                    "You can only update your own events"
            );
        }

        validateEvent(event);

        // Get current number of registrations
        long registeredCount =
                registrationRepository.countByEventId(eventId);

        // Do not allow capacity below current registrations
        if (event.getCapacity() < registeredCount) {

            throw new RuntimeException(
                    "Capacity cannot be less than current registrations"
            );
        }

        existingEvent.setTitle(event.getTitle());
        existingEvent.setDate(event.getDate());
        existingEvent.setVenue(event.getVenue());
        existingEvent.setCapacity(event.getCapacity());

        return eventRepository.save(existingEvent);
    }

    // =====================================================
    // DELETE EVENT
    // =====================================================

    public void deleteEvent(
            Long eventId,
            Long organizerId) {

        Event event =
                eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        Organizer organizer =
                organizerRepository.findById(organizerId).orElse(null);

        if (organizer == null) {
            throw new RuntimeException("Organizer not found");
        }

        // Check ownership
        if (!event.getOrganizer().getId().equals(organizerId)) {

            throw new RuntimeException(
                    "You can only delete your own events"
            );
        }

        // Check registrations
        long registeredCount =
                registrationRepository.countByEventId(eventId);

        if (registeredCount > 0) {

            throw new RuntimeException(
                    "Cannot delete event because students are registered"
            );
        }

        eventRepository.delete(event);
    }

    // =====================================================
    // GET REGISTERED COUNT
    // =====================================================

    public long getRegisteredCount(Long eventId) {

        Event event =
                eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        return registrationRepository.countByEventId(eventId);
    }

    // =====================================================
    // GET AVAILABLE SEATS
    // =====================================================

    public long getAvailableSeats(Long eventId) {

        Event event =
                eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        long registeredCount =
                registrationRepository.countByEventId(eventId);

        long availableSeats =
                event.getCapacity() - registeredCount;

        return Math.max(availableSeats, 0);
    }

    // =====================================================
    // GET EVENT STATUS
    // =====================================================

    public String getEventStatus(Long eventId) {

        Event event =
                eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        long registeredCount =
                registrationRepository.countByEventId(eventId);

        if (registeredCount >= event.getCapacity()) {
            return "FULL";
        }

        return "AVAILABLE";
    }

    // =====================================================
    // VALIDATION
    // =====================================================

    private void validateEvent(Event event) {

        if (event.getTitle() == null
                || event.getTitle().trim().isEmpty()) {

            throw new RuntimeException(
                    "Event title is required"
            );
        }

        if (event.getDate() == null
                || event.getDate().trim().isEmpty()) {

            throw new RuntimeException(
                    "Event date is required"
            );
        }

        if (event.getVenue() == null
                || event.getVenue().trim().isEmpty()) {

            throw new RuntimeException(
                    "Event venue is required"
            );
        }

        if (event.getCapacity() <= 0) {

            throw new RuntimeException(
                    "Event capacity must be greater than 0"
            );
        }
    }
}