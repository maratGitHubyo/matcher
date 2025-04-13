package ru.sloggers.matcher.services.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import ru.sloggers.matcher.services.client.dto.RecognitionResponse;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@FeignClient(name = "recognize-client", url = "${feign.client.config.recognize-client.url}")
public interface RecognitionClient {

    @PostMapping(value = "recognize", produces = MULTIPART_FORM_DATA_VALUE)
    RecognitionResponse recognizePhotos(@RequestPart("oldPhotos") List<Resource> oldPhotos,
                                        @RequestPart("newPhotos") List<Resource> newPhotos);
}
