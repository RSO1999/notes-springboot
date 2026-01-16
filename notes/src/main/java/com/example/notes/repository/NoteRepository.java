// src/main/java/com/example/notes/repository/NoteRepository.java
package com.example.notes.repository;

import com.example.notes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

    // TODO (Prompt D): Add a case-insensitive search method (title OR content).
    // Example idea:
    // Page<Note> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);
}
// FILENAME: NoteRepository.java
// Repository allows us certain methods for free (e.g., save, findAll, findById, deleteById)
// and also allows us to define custom query methods based on method naming conventions.
// It abstracts away the data access layer and lets us focus on business logic in the service layer.
// It autowires the implementation at runtime, so we don't have to write boilerplate code for common operations.
// For it to autowire you just don't write anything.