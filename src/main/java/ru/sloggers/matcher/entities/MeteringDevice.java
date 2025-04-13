package ru.sloggers.matcher.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeteringDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Старый номер счетчика
     */
    private String oldNumber;

    /**
     * Новый номер счетчика
     */
    private String newNumber;

    private String city;

    private String street;

    private String houseNumber;

    private String apartmentNumber;

    private String oldType;

    private String newType;

    /**
     * Список имен/ссылок старых фотографий ПУ на момент обработки документа
     * Дефолтное значение empty
     */
    private String renamedOldPhotos;

    /**
     * Список имен/ссылок новых фотографий ПУ на момент обработки документа
     * Дефолтное значение: empty
     */
    private String renamedNewPhotos;

    /**
     * Путь к папке с необработанными фото
     */
    private String canonicalPhotoPath;

    private Boolean isRecognized;
}
