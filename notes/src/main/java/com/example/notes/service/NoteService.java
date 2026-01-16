// src/main/java/com/example/notes/service/NoteService.java
package com.example.notes.service;

import com.example.notes.dto.NoteCreateRequest;
import com.example.notes.dto.NoteResponse;
import com.example.notes.dto.NoteUpdateRequest;
import com.example.notes.error.ResourceNotFoundException;
import com.example.notes.model.Note;
import com.example.notes.repository.NoteRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    public NoteService(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    // here
    @Transactional
    public NoteResponse create(@Valid NoteCreateRequest request) {
        // TODO (Prompt C): Enforce validation (after adding annotations) and consider trimming whitespace.
        // Keep business logic here, not in the controller.

        Note note = Note.create(request.title(), request.content());
        Note saved = noteRepository.save(note);
        return noteMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<NoteResponse> listAll() {
        // TODO (Prompt E): Replace with Pageable-based retrieval and sorting.
        return noteRepository.findAll()
                .stream()
                .map(noteMapper::toResponse)
                .toList();
    }
//CORRECTION: NO HTTP exceptions in service layer.
    // you want a runtime exception not http

    /*
    In this endpoint getById():
        Status is always 200 if successful
        Errors are handled globally
        No headers needed
        So ResponseEntity adds no value here.
        This is the key distinction 👇
        Controller responsibilities (the real rule)
        * Controllers SHOULD:
        Map HTTP → service call
        Add headers when needed
        Choose status codes when there are multiple valid outcomes
        * Controllers SHOULD NOT:
        Decide business failure semantics
        Translate Optional → exception everywhere
        Duplicate domain rules
     */

//    Service layer:
//            throws domain exceptions (NotFound, Conflict, etc.)

    // TODO (Prompt A): Implement getById(Long id) returning Optional -> mapped to 404 at controller/advice layer.
    @Transactional(readOnly = true)
    public NoteResponse getById(@NotNull Long id) {  // ← returns NoteResponse, not Optional!
        return noteRepository.findById(id)
                .map(noteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("HERE: Note not found with id: " + id));


    }
    // TODO (Prompt B): Implement update(Long id, ...) using optimistic locking behavior and updatedAt.

    @Transactional
    // return note response and accept request
    public NoteResponse updateNote(long id, @Valid NoteUpdateRequest updateRequest) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));

        // Update title and content using the entity method
        note.updateTitleAndContent(updateRequest.title(), updateRequest.content());

        // Save the updated note (this will also update updatedAt via @PreUpdate)
        Note updatedNote = noteRepository.save(note);

        return noteMapper.toResponse(updatedNote);

    }
    // No return value needed
    @Transactional
    public void deleteNote(long id) {
        if (!noteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Note not found with id: " + id);
        }
        noteRepository.deleteById(id);
    }


    // TODO (Prompt D): Implement search(query, pageable) delegating to repository.
}
// to run a spring gradle application use the command: ./gradlew bootRun
// to test the get request in the api run this commmand in the terminal: curl -X GET http://localhost:8080/notes/1

/// ResponseEntity is preferred when the controller needs to control HTTP mechanics.
/// Plain DTO return is preferred when it doesn’t.

//NOTE always accept requests