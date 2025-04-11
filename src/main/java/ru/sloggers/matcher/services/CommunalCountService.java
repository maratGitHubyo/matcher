package ru.sloggers.matcher.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sloggers.matcher.entities.CommunalCounter;
import ru.sloggers.matcher.repositories.CommunalCounterJpaRepository;
import ru.sloggers.matcher.repositories.CommunalCounterRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunalCountService {

    private final CommunalCounterRepository communalCounterRepository;
    private final CommunalCounterJpaRepository communalCounterJpaRepository;

    @Transactional
    public void saveAll(List<CommunalCounter> communalCounters) {
        communalCounterRepository.insertAllIfNotExists(communalCounters);
    }

    @Transactional(readOnly = true)
    public boolean existsByOldNumberAndNewNumber(String oldNumber, String newNumber) {
        return communalCounterJpaRepository.existsByOldNumberAndNewNumber(oldNumber, newNumber);
    }
}
