// src/main/java/com/example/notes/controller/NoteController.java
package com.example.notes.controller;

import com.example.notes.dto.NoteCreateRequest;
import com.example.notes.dto.NoteResponse;
import com.example.notes.dto.NoteUpdateRequest;
import com.example.notes.service.NoteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@Validated
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }
//@valid is a trigger not a validator itself

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse create(@Valid @RequestBody NoteCreateRequest request) {
        // TODO (Prompt C): Add validation annotations to DTOs and return helpful 400 errors.
        // TODO (Prompt F): Introduce a global error handler to standardize validation errors.

        // NOTE: Keeping controller thin: no persistence / business logic here.
        return noteService.create(request);
    }

    @GetMapping
    public List<NoteResponse> listAll() {
        // TODO (Prompt D): Add search: GET /notes?query=foo (case-insensitive; title OR content)
        // TODO (Prompt E): Add pagination & sorting: GET /notes?page=0&size=10&sort=createdAt,desc
        return noteService.listAll();
    }

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
//    Controller layer:
//    should not contain business error logic
//    may return ResponseEntity for headers/status if needed
//    should let exceptions bubble

    /// ResponseEntity is preferred when the controller needs to control HTTP mechanics.
    /// Plain DTO return is preferred when it doesn’t.
    // TODO (Prompt A): Add GET /notes/{id} that returns 404 if not found
    @GetMapping("/{id}")
    public NoteResponse findById(@PathVariable @Positive Long id) {
        return noteService.getById(id);
    }

    // TODO (Prompt B): Add PUT /notes/{id} to update title/content and updatedAt
    @PutMapping("/{id}")
    public NoteResponse update(@PathVariable @Positive Long id, @Valid @RequestBody NoteUpdateRequest request) {
        //delegate to service layer and let it handle the business logic and exceptions
        // send request to service layer and return the response
        return  noteService.updateNote(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @Positive Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

}
// FILENAME: NoteController.java
//NoteController is the entry point for http requests
// they route methods to the service layer
// http methods are routed to the appropriate service method
// there is some validation on the request body using @Valid and @RequestBody
// and will trigger validation based on annotations in the NoteCreateRequest DTO

//always return noteresponse