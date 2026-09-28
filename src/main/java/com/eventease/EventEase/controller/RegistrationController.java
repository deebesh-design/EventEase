package com.eventease.EventEase.controller;

import com.eventease.EventEase.entity.Registration;
import com.eventease.EventEase.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    private final RegistrationService service;

    public RegistrationController(RegistrationService service) {
        this.service = service;
    }

    // =====================================================
    // CREATE REGISTRATION
    // =====================================================

    @PostMapping
    public Registration registerStudent(
            @RequestParam Long studentId,
            @RequestParam Long eventId) {

        return service.registerStudent(studentId, eventId);
    }

    // =====================================================
    // READ ALL REGISTRATIONS
    // =====================================================

    @GetMapping
    public List<Registration> getAllRegistrations() {

        return service.getAllRegistrations();
    }

    // =====================================================
    // READ ONE REGISTRATION
    // =====================================================

    @GetMapping("/{id}")
    public Registration getRegistrationById(
            @PathVariable Long id) {

        return service.getRegistrationById(id);
    }

    // =====================================================
    // UPDATE REGISTRATION
    // =====================================================

    @PutMapping("/{id}")
    public Registration updateRegistration(
            @PathVariable Long id,
            @RequestParam Long studentId,
            @RequestParam Long eventId) {

        return service.updateRegistration(
                id,
                studentId,
                eventId
        );
    }

    // =====================================================
    // DELETE / CANCEL REGISTRATION
    // =====================================================

    @DeleteMapping("/{id}")
    public String deleteRegistration(
            @PathVariable Long id) {

        service.deleteRegistration(id);

        return "Registration cancelled successfully";
    }

    // =====================================================
    // GET REGISTRATIONS FOR AN EVENT
    // =====================================================

    @GetMapping("/event/{eventId}")
    public List<Registration> getRegistrationsByEvent(
            @PathVariable Long eventId) {

        return service.getRegistrationsByEvent(eventId);
    }
}