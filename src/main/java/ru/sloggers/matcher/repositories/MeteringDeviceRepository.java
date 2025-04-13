package ru.sloggers.matcher.repositories;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.sloggers.matcher.entities.MeteringDevice;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MeteringDeviceRepository {

    private final EntityManager entityManager;

    public void insertAllIfNotExists(List<MeteringDevice> counters) {
        for (MeteringDevice counter : counters) {
            entityManager.createNativeQuery("""
                        INSERT INTO metering_device (
                            old_number, new_number, city, street, house_number,
                            apartment_number, old_type, new_type,
                            renamed_old_photos, renamed_new_photos,
                            canonical_photo_path, is_recognized
                        ) VALUES (
                            :oldNumber, :newNumber, :city, :street, :houseNumber,
                            :apartmentNumber, :oldType, :newType,
                            :renamedOldPhotos, :renamedNewPhotos,
                            :canonicalPhotoPath, :isRecognized
                        )
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
                .setParameter("renamedOldPhotos", counter.getRenamedOldPhotos())
                .setParameter("renamedNewPhotos", counter.getRenamedNewPhotos())
                .setParameter("canonicalPhotoPath", counter.getCanonicalPhotoPath())
                .setParameter("isRecognized", counter.getIsRecognized())
                .executeUpdate();
        }
    }
}
