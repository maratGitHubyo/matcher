package ru.sloggers.matcher.integrations;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.sloggers.matcher.dto.RecognitionResponseList;

import java.util.List;

@FeignClient(name = "recognize-client", url = "${feign.client.config.recognize-client.url}")
public interface RecognitionClient {

    @PostMapping(value = "recognize", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RecognitionResponseList recognizePhotos(@RequestPart("files") List<MultipartFile> files);
}
