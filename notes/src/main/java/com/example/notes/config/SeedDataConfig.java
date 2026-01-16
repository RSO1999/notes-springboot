// src/main/java/com/example/notes/config/SeedDataConfig.java
package com.example.notes.config;

import com.example.notes.model.Note;
import com.example.notes.repository.NoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedDataConfig {

    @Bean
    CommandLineRunner seedNotes(NoteRepository noteRepository) {
        return args -> {
            if (noteRepository.count() > 0) {
                return;
            }

            noteRepository.save(Note.create("Welcome", "This is a tiny notes service."));
            noteRepository.save(Note.create("Pairing", "You will extend this in an interview-style session."));
            noteRepository.save(Note.create("TODOs", "Search, pagination, validation, and error handling are intentionally incomplete."));
            noteRepository.save(Note.create("H2", "Use the in-memory H2 DB for fast local testing."));
        };
    }
}
//FILENAME: SeedDataConfig.java
//Seeds the data base
// Allows it to have data when app is started
// @Configuration - indicates that this class contains bean definitions for the application context
// @Bean - indicates that a method produces a bean to be managed by the Spring container
// A bean is, in simple terms, an object that is instantiated, assembled, and
// otherwise managed by a Spring IoC container.
// Beans are the backbone of a Spring application
// and are used to define the objects that make up the application and their dependencies.