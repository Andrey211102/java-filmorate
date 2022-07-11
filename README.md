# Filmorate project
***
## ER Схема

![схема](./src/main/resources/ER.jpg)

#### Описание схемы:
 1. __FILMS__ - содержит данные о фильмах. 
    1.1 В связанной с ней по первичному коючу таблице __LIKES__ хранятся данные о лайках фильмов в разрезе фильмов и пользователей.
    1.2 Рэйтинг MPA будет установлен соответствующим _ENUM_ наименование значения которого хранится в поле __raitingMPA__
2. __GENRES__ - содержит список жанров фильмов

3. __USERS__ - содержит данные о пользователях

4. __FRIENDSHIPS__ - содержит информацию о дружбах пользователей , значение статуса дружбы хранится в   поле __friendshipStatus__ и содержит наименование значения _ENUM_
    
5. __FILMS_GENRES__ - содержит информацию о жанрах фильмов 
_____
####Основные запросы

###### Пользователи

___Получить пользователя по id___
SELECT * FROM USERS WHERE "user_id" = 1

___Получить список друзей___ 
SELECT "friend_id" FROM FRIENDSHIPS WHERE "user_id" = 1

___Добавить в друзья___
INSERT INTO FRIENDSHIPS ("user_id", "friend_id", "friendshipStatus")
VALUES (1,2,'unconfirmed') 

___Удалить из друзей___
DELETE FROM FRIENDSHIPS WHERE "user_id" = 1 AND "friend_id" = 2

###### Фильмы

___Добавить лайк___
INSERT INTO LIKES ("film_id","user_id") 
VALUES (1,1)

___Удалить лайк____
DELETE FROM LIKES WHERE "film_id" = 1 AND "user_id" = 2

___Получить ТОП 10 самых популярных фильмов___
SELECT "film_id"
FROM LIKES
GROUP BY "film_id"
ORDER BY  COUNT("user_id") DESC
LIMIT 10

