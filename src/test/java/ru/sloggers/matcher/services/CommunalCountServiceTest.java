package ru.sloggers.matcher.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sloggers.matcher.entities.CommunalCounter;
import ru.sloggers.matcher.repositories.CommunalCounterJpaRepository;
import ru.sloggers.matcher.repositories.CommunalCounterRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommunalCountServiceTest {

    @Mock
    private CommunalCounterRepository communalCounterRepository;

    @Mock
    private CommunalCounterJpaRepository communalCounterJpaRepository;

    @InjectMocks
    private CommunalCountService service;

    @Test
    void checkIfExistSetRecognizeFlag() {
        //prepare
        String oldNumber = "123";
        String newNumber = "345";
        String address = "Пенза/Коммунистическая/40/0/53";

        CommunalCounter communalCounter = CommunalCounter.builder()
                .oldNumber(oldNumber)
                .newNumber(newNumber)
                .city("г Пенза")
                .street("Коммунистическая")
                .houseNumber("40")
                .apartmentNumber("53")
                .recognized(false)
                .build();

        //mock
        when(communalCounterJpaRepository.existsByOldNumberAndNewNumber(oldNumber, newNumber)).thenReturn(Boolean.TRUE);
        when(communalCounterJpaRepository.findByOldNumberAndNewNumber(oldNumber, newNumber)).thenReturn(communalCounter);
        communalCounter.setRecognized(true);
        when(communalCounterJpaRepository.save(communalCounter)).thenReturn(communalCounter);

        //process
        boolean existSetRecognizeFlag = service.checkIfExistSetRecognizeFlag(oldNumber, newNumber, address);

        //assert
        assertTrue(existSetRecognizeFlag);
        verify(communalCounterJpaRepository, times(1)).save(communalCounter);
    }
}