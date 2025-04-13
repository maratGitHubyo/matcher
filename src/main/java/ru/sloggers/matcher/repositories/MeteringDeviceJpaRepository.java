package ru.sloggers.matcher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.sloggers.matcher.entities.MeteringDevice;

import java.util.Optional;

@Repository
public interface MeteringDeviceJpaRepository extends JpaRepository<MeteringDevice, Long> {

    Boolean existsByOldNumberAndNewNumber(String oldNumber, String newNumber);

    Optional<MeteringDevice> findByOldNumberAndNewNumber(String oldNumber, String newNumber);

    MeteringDevice getByOldNumberAndNewNumber(String oldNumber, String newNumber);

    Boolean existsByOldNumberAndNewNumberAndIsRecognized(String oldNumber, String newNumber, Boolean recognized);

}
