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

    public Registration registerStudent(Long studentId, Long eventId) {

        Student student = studentRepository.findById(studentId).orElse(null);

        if (student == null) {
            return null;
        }

        Event event = eventRepository.findById(eventId).orElse(null);

        if (event == null) {
            return null;
        }

        List<Registration> registrations =
                registrationRepository.findAll();

        for (Registration registration : registrations) {

            if (registration.getStudent().getId().equals(studentId)
                    && registration.getEvent().getId().equals(eventId)) {

                return null;
            }
        }

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

    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public Registration getRegistrationById(Long id) {
        return registrationRepository.findById(id).orElse(null);
    }
}