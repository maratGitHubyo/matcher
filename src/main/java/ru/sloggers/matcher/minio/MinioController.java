package ru.sloggers.matcher.minio;

import io.minio.ObjectWriteResponse;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.sloggers.matcher.dto.ItemDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("minio")
public class MinioController {

    private final MinioService minioService;

    @GetMapping("objects")
    public List<ItemDto> listObjects() {
        return minioService.listObjects();
    }

    @GetMapping("download/{filename}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String filename) throws MinioException {
        InputStream inputStream = minioService.downloadFile(filename);

        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @PostMapping("upload")
    public ResponseEntity<ObjectWriteResponse> uploadFile(@RequestParam("file") MultipartFile file) throws MinioException {
        try {
            ObjectWriteResponse objectWriteResponse = minioService.uploadFile(file.getOriginalFilename(), file.getInputStream(), "image/jpeg");
            return ResponseEntity.ok(objectWriteResponse);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}

