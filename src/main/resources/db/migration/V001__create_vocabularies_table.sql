CREATE TABLE IF NOT EXISTS t_vocabularies
(
    id             VARCHAR(255) NOT NULL PRIMARY KEY,
    word           VARCHAR(255) NOT NULL UNIQUE,
    meaning        VARCHAR(255) NOT NULL,
    pron           VARCHAR(255) NOT NULL,
    part_of_speech VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP(6) NOT NULL,
    study          BOOL         NOT NULL DEFAULT FALSE,
    del            BOOL         NOT NULL DEFAULT FALSE
);
