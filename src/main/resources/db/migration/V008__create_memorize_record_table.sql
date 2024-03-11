CREATE TABLE IF NOT EXISTS t_memorize_records
(
    id         VARCHAR(255) NOT NULL PRIMARY KEY,
    words      TEXT         NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    year       INT2         NOT NULL,
    month      INT2         NOT NULL,
    day        INT2         NOT NULL
);
