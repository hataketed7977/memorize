CREATE TABLE IF NOT EXISTS t_unfamiliar_words
(
    id         VARCHAR(255) NOT NULL PRIMARY KEY,
    dic_id     VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_incorrect_words
(
    id         VARCHAR(255) NOT NULL PRIMARY KEY,
    dic_id     VARCHAR(255) NOT NULL,
    count      INT          NOT NULL DEFAULT 1,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_familiar_words
(
    id         VARCHAR(255) NOT NULL PRIMARY KEY,
    dic_id     VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL
);
