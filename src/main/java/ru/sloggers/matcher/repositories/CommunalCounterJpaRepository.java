package ru.sloggers.matcher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sloggers.matcher.entities.CommunalCounter;

@Repository
public interface CommunalCounterJpaRepository extends JpaRepository<CommunalCounter, Long> {

    Boolean existsByOldNumberAndNewNumber(String oldNumber, String newNumber);
}
