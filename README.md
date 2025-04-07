Описание бд
1) FILM
  FilmId – уникальный идентификатор фильма (PK).
  Name – название фильма.
  Description – описание фильма.
  ReleaseDate – дата выхода.
  Duration – продолжительность (в минутах).
  RatingId – внешний ключ на таблицу Rating.

2) Rating
  RatingId – уникальный идентификатор рейтинга (PK).
  Name – краткое обозначение рейтинга (например, PG-13).
  Description – описание рейтинга.

3) Genre
  GenresId – уникальный идентификатор жанра (PK).
  Name – название жанра (драма, комедия и т.д.).

4) Film_Genre
Связывает фильмы с жанрами.
  FilmId – внешний ключ на FILM.
  GenresId – внешний ключ на Genre.

5) User
  UserID – уникальный идентификатор пользователя (PK).
  Email – почта пользователя.
  Login – логин.
  Name – имя.
  Birthday – дата рождения.

4) Film_Likes
Содержит информацию о том, какие фильмы понравились каким пользователям.
  FilmId – внешний ключ на FILM.
  UserID – внешний ключ на User.

![image](https://github.com/user-attachments/assets/928d9269-2bf8-44ce-9414-a0b9bc918912)

Примеры запросов в Бд:

1) Вывод таблицы с фильмами:
   ```
    SELECT *
    FROM Film
   ```

2) Поиск фильм по id:
   ```
    SELECT *
    FROM Film
    WHERE FilmId = id; --вставьте номер id 
   ```

3) Таблица жанров: 
   ```
    SELECT *
    FROM Genre
    WHERE GeneresId = id; --вставьте номер id 
   ```

4) Вывод имени и email всех пользователей:
   ```
   SELECT Name,
       Emil
    FROM User;
   ```

5) Поиск пользователя по id:
   ```
    SELECT *
    FROM User
    WHERE UserId = id; --вставьте номер id 
   ```


   


