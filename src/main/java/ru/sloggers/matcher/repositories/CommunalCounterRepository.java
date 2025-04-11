package ru.sloggers.matcher.repositories;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.sloggers.matcher.entities.CommunalCounter;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommunalCounterRepository {

    private final EntityManager entityManager;

    public void insertAllIfNotExists(List<CommunalCounter> counters) {
        for (CommunalCounter counter : counters) {
            entityManager.createNativeQuery("""
                                INSERT INTO communal_counter (
                                    old_number, new_number, city, street, house_number,
                                    apartment_number, old_type, new_type
                                ) VALUES (:oldNumber, :newNumber, :city, :street, :houseNumber,
                                          :apartmentNumber, :oldType, :newType)
                                ON CONFLICT (old_number, new_number) DO NOTHING
                            """)
                    .setParameter("oldNumber", counter.getOldNumber())
                    .setParameter("newNumber", counter.getNewNumber())
                    .setParameter("city", counter.getCity())
                    .setParameter("street", counter.getStreet())
                    .setParameter("houseNumber", counter.getHouseNumber())
                    .setParameter("apartmentNumber", counter.getApartmentNumber())
                    .setParameter("oldType", counter.getOldType())
                    .setParameter("newType", counter.getNewType())
                    .executeUpdate();
        }
    }
}
