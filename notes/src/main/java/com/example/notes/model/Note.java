// src/main/java/com/example/notes/model/Note.java
package com.example.notes.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

/*
Annotations in Spring Boot (specifically JPA annotations) can handle unique constraints
at the database schema generation level, but you must still handle potential violations
manually at the application level through exception handling to provide a proper API
response and ensure thread safety.

Validate as early as possible.
 */

@Entity


public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Version annotation is for optimistic locking, which helps prevent lost updates in concurrent scenarios.
    @Version
    private Long version; // JPA nuance: optimistic locking

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = true, length = 500)
    private String content;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = true)
    private Instant updatedAt;

    protected Note() {
        // for JPA
    }

    private Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Note create(String title, String content) {
        return new Note(title, content);
    }

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void updateTitleAndContent(String newTitle, String newContent) {
        // TODO (Prompt B): use this method from a future PUT /notes/{id} service method.
        this.title = newTitle;
        this.content = newContent;
        // updatedAt will be set via @PreUpdate
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note note)) return false;
        return id != null && Objects.equals(id, note.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
//FILENAME: Note.java
// entity directly models the database table.
// It should not contain business logic (e.g. validation) or be used as a DTO for clients.
// Only layer that interacts directly with the database via JPA/Hibernate.
// It should never be exposed outside the service layer.
