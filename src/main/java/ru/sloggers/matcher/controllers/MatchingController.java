package ru.sloggers.matcher.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.sloggers.matcher.services.RegistryParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("matching")
public class MatchingController {

    private final RegistryParser registryParser;

    @PostMapping
    public ResponseEntity<?> match(@RequestParam MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();

        // первый раз
        InputStream input1 = new ByteArrayInputStream(bytes);

        InputStream input2 = new ByteArrayInputStream(bytes);

        registryParser.parse(input1);
//
//        photoParses.process();
//
//        Excel.generate();
        return ResponseEntity.ok().body(registryParser.parse(input2));
    }
}
