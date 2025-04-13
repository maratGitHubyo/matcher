package ru.sloggers.matcher.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sloggers.matcher.repositories.MeteringDeviceJpaRepository;
import ru.sloggers.matcher.repositories.MeteringDeviceRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class MeteringDeviceServiceTest {

    @Mock
    private MeteringDeviceRepository meteringDeviceRepository;

    @Mock
    private MeteringDeviceJpaRepository meteringDeviceJpaRepository;

    @InjectMocks
    private MeteringDeviceService service;

    @Test
    void checkIfExistSetRecognizeFlag() {
        /*//prepare
        String oldNumber = "123";
        String newNumber = "345";
        String address = "Пенза/Коммунистическая/40/0/53";

        MeteringDevice meteringDevice = MeteringDevice.builder()
                .oldNumber(oldNumber)
                .newNumber(newNumber)
                .city("г Пенза")
                .street("Коммунистическая")
                .houseNumber("40")
                .apartmentNumber("53")
                .isRecognized(false)
                .build();

        //mock
        when(communalCounterJpaRepository.findByOldNumberAndNewNumber(oldNumber, newNumber)).thenReturn(Optional.of(meteringDevice));
        meteringDevice.setIsRecognized(true);
        when(communalCounterJpaRepository.save(meteringDevice)).thenReturn(meteringDevice);

        //process
        boolean existSetRecognizeFlag = service.checkIfExistSetRecognizeFlag(oldNumber, newNumber, address);

        //assert
        assertTrue(existSetRecognizeFlag);
        verify(communalCounterJpaRepository, times(1)).save(meteringDevice);*/
    }
}
