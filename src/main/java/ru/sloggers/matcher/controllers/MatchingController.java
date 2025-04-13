package ru.sloggers.matcher.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.sloggers.matcher.services.ExcelService;
import ru.sloggers.matcher.services.PhotoFolderScanner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("matching")
public class MatchingController {

    private final ExcelService excelService;
    private final PhotoFolderScanner scanner;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Сопоставление Excel-файла", description = "Загружает Excel-файл, обрабатывает и возвращает результат")
    public ResponseEntity<byte[]> match(
        @Parameter(description = "Excel-файл", required = true)
        @RequestParam("file") MultipartFile file,
        @RequestParam String path
    ) throws IOException {
        byte[] bytes = file.getBytes();
        InputStream excelForParser = new ByteArrayInputStream(bytes);
        InputStream excelForResult = new ByteArrayInputStream(bytes);
        excelService.parseRegistryAndSaveCounters(excelForParser);
        scanner.process(path);
        byte[] generateMatchingResult = excelService.generateMatchingResult(excelForResult);
        String updatedFilename = "matched_" + file.getOriginalFilename();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + updatedFilename + "\"")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(generateMatchingResult);
    }
}
