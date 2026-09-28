package com.eventease.EventEase.controller;

import com.eventease.EventEase.dto.RegistrationDTO;
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
            @RequestBody RegistrationDTO dto) {

        return service.registerStudent(
                dto.getStudentId(),
                dto.getEventId()
        );
    }

    // =====================================================
    // READ ALL
    // =====================================================

    @GetMapping
    public List<Registration> getAllRegistrations() {

        return service.getAllRegistrations();
    }

    // =====================================================
    // READ ONE
    // =====================================================

    @GetMapping("/{id}")
    public Registration getRegistrationById(
            @PathVariable Long id) {

        return service.getRegistrationById(id);
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public Registration updateRegistration(
            @PathVariable Long id,
            @RequestBody RegistrationDTO dto) {

        return service.updateRegistration(
                id,
                dto.getStudentId(),
                dto.getEventId()
        );
    }

    // =====================================================
    // DELETE / CANCEL
    // =====================================================

    @DeleteMapping("/{id}")
    public String deleteRegistration(
            @PathVariable Long id) {

        service.deleteRegistration(id);

        return "Registration cancelled successfully";
    }

    // =====================================================
    // GET REGISTRATIONS FOR EVENT
    // =====================================================

    @GetMapping("/event/{eventId}")
    public List<Registration> getRegistrationsByEvent(
            @PathVariable Long eventId) {

        return service.getRegistrationsByEvent(eventId);
    }
}