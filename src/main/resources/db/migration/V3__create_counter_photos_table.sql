CREATE TABLE counter_photo
(
    id                  BIGSERIAL PRIMARY KEY,
    communal_counter_id BIGINT        NOT NULL,
    photo_path          VARCHAR(1024) NOT NULL,
    uploaded_at         TIMESTAMP,

    CONSTRAINT fk_counter
        FOREIGN KEY (communal_counter_id)
            REFERENCES communal_counter (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_counter_photos_counter_id ON counter_photo (communal_counter_id);