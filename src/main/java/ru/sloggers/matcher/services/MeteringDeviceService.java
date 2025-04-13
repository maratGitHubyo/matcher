package ru.sloggers.matcher.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sloggers.matcher.entities.MeteringDevice;
import ru.sloggers.matcher.repositories.MeteringDeviceJpaRepository;
import ru.sloggers.matcher.repositories.MeteringDeviceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeteringDeviceService {

    private final MeteringDeviceRepository meteringDeviceRepository;
    private final MeteringDeviceJpaRepository meteringDeviceJpaRepository;

    @Transactional
    public void saveAll(List<MeteringDevice> meteringDevices) {
        meteringDeviceRepository.insertAllIfNotExists(meteringDevices);
    }

    @Transactional(readOnly = true)
    public boolean existsByOldNumberAndNewNumber(String oldNumber, String newNumber) {
        return meteringDeviceJpaRepository.existsByOldNumberAndNewNumber(oldNumber, newNumber);
    }

    @Transactional(readOnly = true)
    public boolean existsByOldNumberAndNewNumberAndRecognized(String oldNumber, String newNumber) {
        return meteringDeviceJpaRepository.existsByOldNumberAndNewNumberAndIsRecognized(oldNumber, newNumber, true);
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
    public boolean checkIfExistSetRecognizeFlag(String oldNumber, String newNumber, String[] address) {
        var meteringDevice = meteringDeviceJpaRepository.findByOldNumberAndNewNumber(oldNumber, newNumber)
                .orElseThrow(() -> new RuntimeException("Communal counter with oldNumber = '%s' and newNumber = '%s' not found".formatted(oldNumber, newNumber)));

        if (isExactMatch(oldNumber, newNumber, meteringDevice, address)) {
            meteringDevice.setIsRecognized(true);
            meteringDeviceJpaRepository.save(meteringDevice);
            return true;
        } else {
            throw new RuntimeException(("Распознанные данные не совпадают с данными реестра, address = '%s'," +
                    " oldNumber = '%s' and newNumber = '%s'").formatted(address, oldNumber, newNumber));
        }
    }

    // TODO : добавит тримминг строки при парсинге excel
    private static boolean isExactMatch(String oldNumber, String newNumber, MeteringDevice meteringDevice, String[] address) {
        return meteringDevice.getCity().toLowerCase().contains(address[0].toLowerCase()) &&
                meteringDevice.getStreet().toLowerCase().contains(address[1].toLowerCase()) &&
                meteringDevice.getHouseNumber().toLowerCase().contains(address[2].toLowerCase()) &&
                meteringDevice.getApartmentNumber().toLowerCase().contains(address[4].toLowerCase()) &&
                meteringDevice.getOldNumber().equals(oldNumber) &&
                meteringDevice.getNewNumber().equals(newNumber);
    }
}
