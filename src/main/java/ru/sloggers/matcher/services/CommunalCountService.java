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

    @Transactional(readOnly = true)
    public boolean existsByOldNumberAndNewNumberAndRecognized(String oldNumber, String newNumber) {
        return communalCounterJpaRepository.existsByOldNumberAndNewNumberAndRecognized(oldNumber, newNumber, true);
    }

    /**
     * Метод проверяет полное соответствие адреса и номеров счетчика, если проверка пройдена, то ставит флаг recognize = true
     * Адрес должен выглядеть так: city/street/houseNumber/0/apartmentNumber
     *
     * @param oldNumber номер старого счетчика
     * @param newNumber номер нового счетчика
     * @param address   адрес по которому найдены фото
     * @return true, если совпадение найдено, иначе false
     */
    @Transactional
    public boolean checkIfExistSetRecognizeFlag(String oldNumber, String newNumber, String address) {
        CommunalCounter communalCounter = communalCounterJpaRepository.findByOldNumberAndNewNumber(oldNumber, newNumber)
                .orElseThrow(() -> new RuntimeException("Communal counter with oldNumber = '%s' and newNumber = '%s' not found".formatted(oldNumber, newNumber)));

        if (isExactMatch(oldNumber, newNumber, communalCounter, address)) {
            communalCounter.setRecognized(true);
            communalCounterJpaRepository.save(communalCounter);
            return true;
        } else {
            throw new RuntimeException(("Распознанные данные не совпадают с данными реестра, address = '%s'," +
                    " oldNumber = '%s' and newNumber = '%s'").formatted(address, oldNumber, newNumber));
        }
    }

    private static boolean isExactMatch(String oldNumber, String newNumber, CommunalCounter communalCounter, String address) {
        String[] addressArray = address.split("/");
        return communalCounter.getCity().toLowerCase().contains(addressArray[0].toLowerCase()) && communalCounter.getStreet().toLowerCase().contains(addressArray[1].toLowerCase()) &&
                communalCounter.getHouseNumber().toLowerCase().contains(addressArray[2].toLowerCase()) && communalCounter.getApartmentNumber().toLowerCase().contains(addressArray[4].toLowerCase()) &&
                communalCounter.getOldNumber().equals(oldNumber) && communalCounter.getNewNumber().equals(newNumber);
    }
}
