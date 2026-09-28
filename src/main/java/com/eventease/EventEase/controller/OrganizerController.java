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

    // CREATE
    @PostMapping
    public Organizer addOrganizer(@RequestBody Organizer organizer) {
        return service.addOrganizer(organizer);
    }

    // READ ALL
    @GetMapping
    public List<Organizer> getAllOrganizers() {
        return service.getAllOrganizers();
    }

    // READ ONE
    @GetMapping("/{id}")
    public Organizer getOrganizerById(@PathVariable Long id) {
        return service.getOrganizerById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Organizer updateOrganizer(
            @PathVariable Long id,
            @RequestBody Organizer organizer) {

        return service.updateOrganizer(id, organizer);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteOrganizer(@PathVariable Long id) {

        service.deleteOrganizer(id);

        return "Organizer deleted successfully";
    }
}