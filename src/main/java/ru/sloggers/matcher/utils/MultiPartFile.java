package ru.sloggers.matcher.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MultiPartFile {
    public static MultipartFile convertPathToMultipartFile(Path path) {
        String name = path.getFileName().toString();
        String contentType; // можно null
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] content;
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new MockMultipartFile("file", name, contentType, content);
    }
}
