CREATE TABLE csv_files
(
    csv_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE csv_fields
(
     entry_date    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     csv_name      VARCHAR(255),
     field_name    VARCHAR(255) NOT NULL,
     field_type    VARCHAR(255) NOT NULL,
     is_null_allowed VARCHAR(255),
     field_length  VARCHAR(255),
     field_values  VARCHAR(255),
     dependent_field VARCHAR(255),
     dependent_value VARCHAR(255),
     date_type VARCHAR(255),
     time_type VARCHAR(255),
     datetime VARCHAR(255),
     CONSTRAINT fk_config
        FOREIGN KEY (csv_name)
            REFERENCES csv_files(csv_name)
);

CREATE TABLE field_values
(
    csv_name  VARCHAR(255),
    field_name VARCHAR(255),
    allowed_value VARCHAR(255),
    CONSTRAINT fkey_csv
        FOREIGN KEY (csv_name)
            REFERENCES csv_fields(csv_name)
            ON DELETE CASCADE
);
