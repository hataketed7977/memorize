CREATE VIEW v_incorrect_vocabularies AS
SELECT i.id             as id,
       i.count          as count,
       i.created_at     as incorrect_created_at,
       i.updated_at     as incorrect_updated_at,
       v.word           as word,
       v.meaning        as meaning,
       v.pron           as pron,
       v.part_of_speech as part_of_speech,
       v.id             as vocabulary_id
FROM t_incorrect_words i
         LEFT JOIN t_vocabularies v ON i.vocabulary_id = v.id;


CREATE VIEW v_familiar_vocabularies AS
SELECT f.id             as id,
       f.created_at     as familiar_created_at,
       f.round1         as round1,
       f.round2         as round2,
       f.round3         as round3,
       f.round4         as round4,
       v.word           as word,
       v.meaning        as meaning,
       v.pron           as pron,
       v.part_of_speech as part_of_speech,
       v.id             as vocabulary_id
FROM t_familiar_words f
         LEFT JOIN t_vocabularies v ON f.vocabulary_id = v.id;
