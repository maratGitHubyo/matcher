-- V1__create_communal_counter_table.sql

CREATE TABLE communal_counter
(
    id               BIGSERIAL PRIMARY KEY,
    old_number       VARCHAR(255),
    new_number       VARCHAR(255),
    city             VARCHAR(255),
    street           VARCHAR(255),
    house_number     VARCHAR(255),
    apartment_number VARCHAR(255),
    old_type         VARCHAR(255),
    new_type         VARCHAR(255)
);

-- Добавим уникальный индекс на старый и новый номер
CREATE UNIQUE INDEX idx_communal_counter_old_new_number
    ON communal_counter (old_number, new_number);
