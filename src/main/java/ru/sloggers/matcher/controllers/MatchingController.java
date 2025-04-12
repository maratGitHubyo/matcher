package ru.sloggers.matcher.controllers;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("matching")
public class MatchingController {

    private final ExcelService excelService;

    //TODO: основной бизнес процесс
    @PostMapping
    public ResponseEntity<byte[]> match(@RequestParam MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();

        // первый раз
        InputStream input1 = new ByteArrayInputStream(bytes);

        InputStream input2 = new ByteArrayInputStream(bytes);

        excelService.parseRegistryAndSaveCounters(input1);
//
//        photoParses.process();
//
//        Excel.generate();

        byte[] bytes1 = excelService.generateMatchingResult(input2);

        String updatedFilename = "matched_" + file.getOriginalFilename();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + updatedFilename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes1);
    }
}
