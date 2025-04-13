CREATE SCHEMA IF NOT EXISTS base_schema;
DROP TABLE IF EXISTS metering_device CASCADE;

CREATE TABLE metering_device
(
    id                   BIGSERIAL PRIMARY KEY NOT NULL,

    -- TODO : сделать составной ключ на будущее
    old_number           VARCHAR(255)          NOT NULL,
    new_number           VARCHAR(255)          NOT NULL,
    city                 VARCHAR(255)          NOT NULL,
    street               VARCHAR(255)          NOT NULL,
    house_number         VARCHAR(255)          NOT NULL,
    apartment_number     VARCHAR(255),
    old_type             VARCHAR(255),
    new_type             VARCHAR(255),

    -- TODO : (временно) нормализовать БД и вынести поля ниже в табличку
    -- TODO : (временно) чистить нижние поля после формирования итогового отчета
    renaiming_old_photos  VARCHAR,
    renaiming_new_photos  VARCHAR,
    canonical_photo_path VARCHAR(255),
    is_recognized        BOOLEAN DEFAULT 'false'
);

COMMENT ON TABLE metering_device IS 'Таблица для приборов учета(ПУ)';

CREATE
    UNIQUE INDEX idx_metering_device_old_new_number
    ON metering_device (old_number, new_number);
