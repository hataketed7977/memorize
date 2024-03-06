CREATE TABLE IF NOT EXISTS t_unfamiliar_words
(
    id            VARCHAR(255) NOT NULL PRIMARY KEY,
    vocabulary_id VARCHAR(255) NOT NULL UNIQUE,
    created_at    TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_incorrect_words
(
    id            VARCHAR(255) NOT NULL PRIMARY KEY,
    vocabulary_id VARCHAR(255) NOT NULL UNIQUE,
    count         INT          NOT NULL DEFAULT 1,
    created_at    TIMESTAMP(6) NOT NULL,
    updated_at    TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_familiar_words
(
    id            VARCHAR(255) NOT NULL PRIMARY KEY,
    vocabulary_id VARCHAR(255) NOT NULL UNIQUE,
    created_at    TIMESTAMP(6) NOT NULL
);
