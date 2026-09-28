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

    // CREATE
    public Organizer addOrganizer(Organizer organizer) {

        if (organizer.getName() == null
                || organizer.getName().trim().isEmpty()) {

            throw new RuntimeException("Organizer name is required");
        }

        if (organizer.getEmail() == null
                || organizer.getEmail().trim().isEmpty()) {

            throw new RuntimeException("Organizer email is required");
        }

        if (organizer.getPassword() == null
                || organizer.getPassword().trim().isEmpty()) {

            throw new RuntimeException("Organizer password is required");
        }

        return repository.save(organizer);
    }

    // READ ALL
    public List<Organizer> getAllOrganizers() {
        return repository.findAll();
    }

    // READ ONE
    public Organizer getOrganizerById(Long id) {

        Organizer organizer =
                repository.findById(id).orElse(null);

        if (organizer == null) {
            throw new RuntimeException("Organizer not found");
        }

        return organizer;
    }

    // UPDATE
    public Organizer updateOrganizer(
            Long id,
            Organizer organizer) {

        Organizer existingOrganizer =
                repository.findById(id).orElse(null);

        if (existingOrganizer == null) {
            throw new RuntimeException("Organizer not found");
        }

        if (organizer.getName() == null
                || organizer.getName().trim().isEmpty()) {

            throw new RuntimeException("Organizer name is required");
        }

        if (organizer.getEmail() == null
                || organizer.getEmail().trim().isEmpty()) {

            throw new RuntimeException("Organizer email is required");
        }

        existingOrganizer.setName(organizer.getName());
        existingOrganizer.setEmail(organizer.getEmail());

        if (organizer.getPassword() != null
                && !organizer.getPassword().trim().isEmpty()) {

            existingOrganizer.setPassword(
                    organizer.getPassword()
            );
        }

        return repository.save(existingOrganizer);
    }

    // DELETE
    public void deleteOrganizer(Long id) {

        Organizer organizer =
                repository.findById(id).orElse(null);

        if (organizer == null) {
            throw new RuntimeException("Organizer not found");
        }

        repository.delete(organizer);
    }

    // LOGIN
    public Organizer login(
            String email,
            String password) {

        Organizer organizer =
                repository.findByEmail(email);

        if (organizer == null) {
            throw new RuntimeException("Organizer not found");
        }

        if (!organizer.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return organizer;
    }
}