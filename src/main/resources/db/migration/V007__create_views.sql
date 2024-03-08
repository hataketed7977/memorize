CREATE VIEW v_incorrect_vocabularies AS
SELECT i.count,
       i.created_at as incorrect_created_at,
       i.updated_at as incorrect_updated_at,
       v.*
FROM t_incorrect_words i
         LEFT JOIN t_vocabularies v ON i.vocabulary_id = v.id;


CREATE VIEW v_familiar_vocabularies AS
SELECT f.created_at as familiar_created_at,
       f.round1,
       f.round2,
       f.round3,
       f.round4,
       v.*
FROM t_familiar_words f
         LEFT JOIN t_vocabularies v ON f.vocabulary_id = v.id;
