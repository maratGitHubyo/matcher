package ru.sloggers.matcher.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sloggers.matcher.dto.RecognitionResponse;
import ru.sloggers.matcher.integrations.RecognitionClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecognitionService {

    private final RecognitionClient recognitionClient;

    public List<RecognitionResponse> recognizePhotos(List<MultipartFile> files) {
        return recognitionClient.recognizePhotos(files).recognitions();
    }
}
