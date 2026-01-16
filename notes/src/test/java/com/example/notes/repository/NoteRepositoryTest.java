// src/test/java/com/example/notes/repository/NoteRepositoryTest.java
package com.example.notes.repository;

import com.example.notes.model.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Test
    void savingANote_setsCreatedAt() {
        Note saved = noteRepository.save(Note.create("Test", "CreatedAt should be set automatically"));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNull();
    }
}

