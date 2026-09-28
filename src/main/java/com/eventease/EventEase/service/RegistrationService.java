package com.eventease.EventEase.service;

import com.eventease.EventEase.entity.Event;
import com.eventease.EventEase.entity.Registration;
import com.eventease.EventEase.entity.Student;
import com.eventease.EventEase.repository.EventRepository;
import com.eventease.EventEase.repository.RegistrationRepository;
import com.eventease.EventEase.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final EventRepository eventRepository;

    public RegistrationService(
            RegistrationRepository registrationRepository,
            StudentRepository studentRepository,
            EventRepository eventRepository) {

        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
        this.eventRepository = eventRepository;
    }

    // =====================================================
    // CREATE REGISTRATION
    // =====================================================

    public Registration registerStudent(Long studentId, Long eventId) {

        Student student =
                studentRepository.findById(studentId).orElse(null);

        if (student == null) {
            throw new RuntimeException("Student not found");
        }

        Event event =
                eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        List<Registration> registrations =
                registrationRepository.findAll();

        // Check duplicate registration
        for (Registration registration : registrations) {

            if (registration.getStudent().getId().equals(studentId)
                    && registration.getEvent().getId().equals(eventId)) {

                throw new RuntimeException(
                        "Student already registered for this event"
                );
            }
        }

        // Check event capacity
        int registeredCount = 0;

        for (Registration registration : registrations) {

            if (registration.getEvent().getId().equals(eventId)) {
                registeredCount++;
            }
        }

        if (registeredCount >= event.getCapacity()) {
            throw new RuntimeException("Event is full");
        }

        Registration registration = new Registration();

        registration.setStudent(student);
        registration.setEvent(event);

        return registrationRepository.save(registration);
    }

    // =====================================================
    // READ ALL
    // =====================================================

    public List<Registration> getAllRegistrations() {

        return registrationRepository.findAll();
    }

    // =====================================================
    // READ ONE
    // =====================================================

    public Registration getRegistrationById(Long id) {

        Registration registration =
                registrationRepository.findById(id).orElse(null);

        if (registration == null) {
            throw new RuntimeException("Registration not found");
        }

        return registration;
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public Registration updateRegistration(
            Long id,
            Long studentId,
            Long eventId) {

        Registration existingRegistration =
                registrationRepository.findById(id).orElse(null);

        if (existingRegistration == null) {
            throw new RuntimeException("Registration not found");
        }

        Student student =
                studentRepository.findById(studentId).orElse(null);

        if (student == null) {
            throw new RuntimeException("Student not found");
        }

        Event event =
                eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        List<Registration> registrations =
                registrationRepository.findAll();

        // Check duplicate registration
        for (Registration registration : registrations) {

            if (!registration.getId().equals(id)
                    && registration.getStudent().getId().equals(studentId)
                    && registration.getEvent().getId().equals(eventId)) {

                throw new RuntimeException(
                        "Student already registered for this event"
                );
            }
        }

        // Check capacity
        int registeredCount = 0;

        for (Registration registration : registrations) {

            if (registration.getEvent().getId().equals(eventId)
                    && !registration.getId().equals(id)) {

                registeredCount++;
            }
        }

        if (registeredCount >= event.getCapacity()) {
            throw new RuntimeException("Event is full");
        }

        existingRegistration.setStudent(student);
        existingRegistration.setEvent(event);

        return registrationRepository.save(existingRegistration);
    }

    // =====================================================
    // DELETE / CANCEL REGISTRATION
    // =====================================================

    public void deleteRegistration(Long id) {

        Registration registration =
                registrationRepository.findById(id).orElse(null);

        if (registration == null) {
            throw new RuntimeException("Registration not found");
        }

        Event event = registration.getEvent();

        // Convert event date from String to LocalDate
        LocalDate eventDate;

        try {
            eventDate = LocalDate.parse(event.getDate());
        } catch (Exception e) {
            throw new RuntimeException(
                    "Invalid event date. Use format YYYY-MM-DD"
            );
        }

        // Get today's date
        LocalDate today = LocalDate.now();

        // Cancellation is allowed only before event date
        if (!today.isBefore(eventDate)) {

            throw new RuntimeException(
                    "Registration cannot be cancelled on or after the event date"
            );
        }

        registrationRepository.delete(registration);
    }

    // =====================================================
    // GET REGISTRATIONS FOR EVENT
    // =====================================================

    public List<Registration> getRegistrationsByEvent(Long eventId) {

        Event event =
                eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            throw new RuntimeException("Event not found");
        }

        return registrationRepository.findByEventId(eventId);
    }
}