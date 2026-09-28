package com.eventease.EventEase.service;

import com.eventease.EventEase.entity.Event;
import com.eventease.EventEase.entity.Registration;
import com.eventease.EventEase.entity.Student;
import com.eventease.EventEase.repository.EventRepository;
import com.eventease.EventEase.repository.RegistrationRepository;
import com.eventease.EventEase.repository.StudentRepository;
import org.springframework.stereotype.Service;

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
            return null;
        }

        Event event =
                eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            return null;
        }

        List<Registration> registrations =
                registrationRepository.findAll();

        // Check duplicate registration
        for (Registration registration : registrations) {

            if (registration.getStudent().getId().equals(studentId)
                    && registration.getEvent().getId().equals(eventId)) {

                return null;
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
            return null;
        }

        Registration registration = new Registration();

        registration.setStudent(student);
        registration.setEvent(event);

        return registrationRepository.save(registration);
    }

    // =====================================================
    // READ ALL REGISTRATIONS
    // =====================================================

    public List<Registration> getAllRegistrations() {

        return registrationRepository.findAll();
    }

    // =====================================================
    // READ ONE REGISTRATION
    // =====================================================

    public Registration getRegistrationById(Long id) {

        return registrationRepository
                .findById(id)
                .orElse(null);
    }

    // =====================================================
    // UPDATE REGISTRATION
    // =====================================================

    public Registration updateRegistration(
            Long id,
            Long studentId,
            Long eventId) {

        Registration existingRegistration =
                registrationRepository.findById(id).orElse(null);

        if (existingRegistration == null) {
            return null;
        }

        Student student =
                studentRepository.findById(studentId).orElse(null);

        if (student == null) {
            return null;
        }

        Event event =
                eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            return null;
        }

        List<Registration> registrations =
                registrationRepository.findAll();

        // Check duplicate registration
        for (Registration registration : registrations) {

            if (!registration.getId().equals(id)
                    && registration.getStudent().getId().equals(studentId)
                    && registration.getEvent().getId().equals(eventId)) {

                return null;
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
            return null;
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

        if (registration != null) {

            registrationRepository.delete(registration);
        }
    }

    // =====================================================
    // GET REGISTRATIONS FOR AN EVENT
    // =====================================================

    public List<Registration> getRegistrationsByEvent(Long eventId) {

        return registrationRepository.findByEventId(eventId);
    }
}