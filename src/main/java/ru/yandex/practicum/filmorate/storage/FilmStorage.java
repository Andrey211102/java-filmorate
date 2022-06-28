package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    public Film create(Film newFilm);
    public Film update(Film film);
    public Film getFilmById(long id);

    List<Film> getAll();

}
