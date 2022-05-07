CREATE TABLE csv_files
(
    csv_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE field_values
(
    csv_name VARCHAR(255);
    allowed_value    VARCHAR(255),
    CONSTRAINT fk_csv
        FOREIGN KEY (csv_name)
            REFERENCES csv_file(csv_name)
            ON DELETE CASCADE
);
