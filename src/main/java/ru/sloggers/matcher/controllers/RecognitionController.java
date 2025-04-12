package ru.sloggers.matcher.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.sloggers.matcher.dto.RecognitionRequest;
import ru.sloggers.matcher.dto.RecognitionResponse;
import ru.sloggers.matcher.integrations.RecognitionClient;

import java.util.List;

@RestController
@RequestMapping("recognize")
@RequiredArgsConstructor
public class RecognitionController {

    private final RecognitionClient recognitionClient;

    @PostMapping
    public List<RecognitionResponse> recognize(@RequestParam("files") RecognitionRequest files) {
        return recognitionClient.recognizePhotos(files).recognitions();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFiles(@RequestPart("files") List<MultipartFile> files) {
        StringBuilder response = new StringBuilder("Получено файлов: " + files.size() + "\n");

        for (MultipartFile file : files) {
            response.append("Файл: ").append(file.getOriginalFilename())
                    .append(" (").append(file.getSize()).append(" байт)\n");
            // здесь можно сохранять файл, например, file.transferTo(...)
        }

        return ResponseEntity.ok(response.toString());
    }
}
