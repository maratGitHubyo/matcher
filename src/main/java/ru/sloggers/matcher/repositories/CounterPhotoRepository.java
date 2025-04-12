package ru.sloggers.matcher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sloggers.matcher.entities.CounterPhoto;

@Repository
public interface CounterPhotoRepository extends JpaRepository<CounterPhoto, Long> {

    CounterPhoto findAllByCommunalCounterId(Long communalCounterId);
}
