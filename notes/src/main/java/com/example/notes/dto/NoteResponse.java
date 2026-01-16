// src/main/java/com/example/notes/dto/NoteResponse.java
package com.example.notes.dto;

import java.time.Instant;

public record NoteResponse(
        Long id,
        String title,
        String content,
        Instant createdAt,
        Instant updatedAt
) {
}
// FILENAME: NoteResponse.java
// Note response dto models data we send to clients.
