CREATE TABLE processing_error
(
    id          BIGSERIAL PRIMARY KEY,
    message     VARCHAR(255),
    images_path VARCHAR(255),
    old_number  VARCHAR(255),
    new_number  VARCHAR(255)
);