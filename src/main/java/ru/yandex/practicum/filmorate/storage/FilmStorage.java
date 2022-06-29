package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film create(Film newFilm);
    Film update(Film film);
    void delete(Film film);
    Film getFilmById(long id);

    List<Film> getAll();
}
