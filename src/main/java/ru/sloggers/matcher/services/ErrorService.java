package ru.sloggers.matcher.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sloggers.matcher.entities.ProcessingError;
import ru.sloggers.matcher.repositories.ProcessingErrorRepository;

@Service
@RequiredArgsConstructor
public class ErrorService {

    private final ProcessingErrorRepository processingErrorRepository;

    @Transactional
    public ProcessingError save(ProcessingError error) {
        return processingErrorRepository.save(error);
    }
}
