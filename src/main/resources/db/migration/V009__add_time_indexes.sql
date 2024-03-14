CREATE INDEX t_familiar_words_created_at_index ON t_familiar_words (created_at);

CREATE INDEX t_incorrect_words_created_at_index ON t_incorrect_words (created_at);

CREATE INDEX t_incorrect_words_updated_at_desc_index ON t_incorrect_words (updated_at DESC);
