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

    @PostMapping
    public Registration registerStudent(
            @RequestParam Long studentId,
            @RequestParam Long eventId) {

        return service.registerStudent(studentId, eventId);
    }

    @GetMapping
    public List<Registration> getAllRegistrations() {
        return service.getAllRegistrations();
    }

    @GetMapping("/{id}")
    public Registration getRegistrationById(@PathVariable Long id) {
        return service.getRegistrationById(id);
    }
}