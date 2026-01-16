// src/main/java/com/example/notes/dto/NoteCreateRequest.java
package com.example.notes.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoteCreateRequest(

        @NotBlank @Size(max = 100) String title,
        @NotBlank @Size(max = 500) String content
) {
    // TODO (Prompt C):
    //  - title required, max length 100
    //  - content max length 500
    //  Add jakarta.validation constraints here (e.g., @NotBlank, @Size).
}
// FILENAME: NoteCreateRequest.java
// dto allows us to create a barrier between our internal data model and the external API contract.
// This way, we can evolve our internal Note entity without breaking API clients,
// and we can also enforce validation rules on incoming data.

// this dto models create request