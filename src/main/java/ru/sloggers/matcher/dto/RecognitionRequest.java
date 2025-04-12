package ru.sloggers.matcher.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record RecognitionRequest(
        List<MultipartFile> oldPhotos,
        List<MultipartFile> newPhotos
) {
}
