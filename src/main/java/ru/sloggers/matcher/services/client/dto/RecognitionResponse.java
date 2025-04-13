package ru.sloggers.matcher.services.client.dto;

import lombok.Builder;

@Builder
public record RecognitionResponse(
        String oldNumber,
        String newNumber,
        String errorMessage
) {
}
