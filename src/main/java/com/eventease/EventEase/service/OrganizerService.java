package com.eventease.EventEase.service;

import com.eventease.EventEase.entity.Organizer;
import com.eventease.EventEase.repository.OrganizerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizerService {
    private final OrganizerRepository repository;
    public OrganizerService(OrganizerRepository repository) {
        this.repository = repository;
    }
    public Organizer addOrganizer(Organizer organizer) {
        return repository.save(organizer);
    }
    public List<Organizer> getAllOrganizers() {
        return repository.findAll();
    }
    public Organizer getOrganizerById(Long id) {
        return repository.findById(id).orElse(null);
    }
    public Organizer updateOrganizer(Long id, Organizer organizer) {

        Organizer existingOrganizer = repository.findById(id).orElse(null);

        if (existingOrganizer == null) {
            return null;
        }

        existingOrganizer.setName(organizer.getName());
        existingOrganizer.setEmail(organizer.getEmail());

        return repository.save(existingOrganizer);
    }
    public void deleteOrganizer(Long id) {
        repository.deleteById(id);
    }
}