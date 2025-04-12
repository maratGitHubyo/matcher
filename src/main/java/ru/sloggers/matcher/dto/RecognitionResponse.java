package ru.sloggers.matcher.dto;

import lombok.Builder;

@Builder
public record RecognitionResponse(
        String oldNumber,
        String newNumber,
        String errorMessage
) {
}
