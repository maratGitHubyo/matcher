package ru.sloggers.matcher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sloggers.matcher.entities.ProcessingError;

@Repository
public interface ProcessingErrorRepository extends JpaRepository<ProcessingError, Long> {
}
