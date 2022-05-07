CREATE TABLE csv_file
(
    csv_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE csv_fields
(
    field_id      SERIAL PRIMARY KEY,
    csv_name      VARCHAR(255),
    field_name    VARCHAR(255) NOT NULL,
    field_type    VARCHAR(255) NOT NULL,
    is_null_allowed VARCHAR(255),
    field_length  INT,
    field_values  VARCHAR(255),
    dependent_field VARCHAR(255),
    dependent_value VARCHAR(255),
    CONSTRAINT fk_csv
        FOREIGN KEY (csv_name)
            REFERENCES csv_file(csv_name)
            ON DELETE CASCADE
);
