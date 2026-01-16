package com.example.notes.dto;

import jakarta.validation.constraints.NotBlank;

public record NoteUpdateRequest(
        @NotBlank String title,
        @NotBlank String content
) {
}