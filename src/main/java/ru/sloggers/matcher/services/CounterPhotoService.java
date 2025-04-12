package ru.sloggers.matcher.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sloggers.matcher.entities.CounterPhoto;
import ru.sloggers.matcher.repositories.CounterPhotoRepository;

@Service
@RequiredArgsConstructor
public class CounterPhotoService {

    private final CounterPhotoRepository counterPhotoRepository;

    public CounterPhoto save(CounterPhoto counterPhoto) {
        return counterPhotoRepository.save(counterPhoto);
    }

    public CounterPhoto findByCommunalCounterId(Long counterId) {
        return counterPhotoRepository.findAllByCommunalCounterId(counterId);
    }
}
