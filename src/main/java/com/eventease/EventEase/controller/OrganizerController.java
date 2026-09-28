package com.eventease.EventEase.controller;

import com.eventease.EventEase.entity.Organizer;
import com.eventease.EventEase.service.OrganizerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizers")
public class OrganizerController {

    private final OrganizerService service;

    public OrganizerController(OrganizerService service) {
        this.service = service;
    }

    @PostMapping
    public Organizer addOrganizer(@RequestBody Organizer organizer) {
        return service.addOrganizer(organizer);
    }

    @GetMapping
    public List<Organizer> getAllOrganizers() {
        return service.getAllOrganizers();
    }

    @GetMapping("/{id}")
    public Organizer getOrganizerById(@PathVariable Long id) {
        return service.getOrganizerById(id);
    }
}