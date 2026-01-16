// src/main/java/com/example/notes/service/NoteMapper.java
package com.example.notes.service;

import com.example.notes.dto.NoteResponse;
import com.example.notes.model.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public NoteResponse toResponse(Note note) {
        return new NoteResponse(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }
}
