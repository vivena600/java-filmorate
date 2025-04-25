-- G - у фильма нет возрастных ограничений
-- PG - детям рекомендуется смотреть фильм с родителями
-- PG-13 - детям до 13 лет просмотр не желателен
-- R - лицам ло 17 лет просматривать фильм можно только в присутствии взрослого
-- NC-17 - лицам до 17 лет просмотр запрещен
MERGE INTO mpa AS target USING (
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17')
    ) AS source(mpa_id, name) ON target.MPA_ID = source.mpa_id
WHEN NOT MATCHED THEN INSERT (mpa_id, name) VALUES (source.mpa_id, source.name);

MERGE INTO genres AS target USING (
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик')
    ) AS source(id, name) ON target.id = source.id
WHEN NOT MATCHED THEN INSERT (id, name) VALUES (source.id, source.name);