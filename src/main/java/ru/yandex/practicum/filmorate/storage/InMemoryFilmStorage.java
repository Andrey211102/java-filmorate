package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.exeptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.InvalidValidationExeption;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Long lastUid;
    private final HashMap<Long, Film> films;

    public InMemoryFilmStorage() {
        this.films = new HashMap<>();
        this.lastUid = 1L;
    }

    @Override
    public Film create(Film newFilm) {

        validate(newFilm, RequestMethod.POST);
        newFilm.setId(this.lastUid);

        this.films.put(newFilm.getId(), newFilm);
        log.info("Добавлен фильм: {}", newFilm);

        setLastUid();

        return newFilm;
    }

    @Override
    public Film update(Film film) {

        validate(film, RequestMethod.PUT);

        this.films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);

        return film;
    }

    @Override
    public Film getFilmById(long id) {

        if (!this.films.containsKey(id)) {
            throw new FilmNotFoundException("Ошибка получения фильма по id:" + id);
        }
        return this.films.get(id);
    }

    public List<Film> getAll() {
        return new ArrayList<>(this.films.values());
    }

    private void setLastUid() {

        if (this.films.isEmpty()) {
            this.lastUid = 1L;
        } else {
            this.lastUid++;
        }
    }

    private void validate(Film film, RequestMethod method) {

        String errorMessage;

        if (film.getDescription().length() > 200) {

            errorMessage = "Превышена максимальная длина описания!";
            log.error(errorMessage);
            throw new InvalidValidationExeption(errorMessage);
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {

            errorMessage = "Дата релиза раньше 28.12.1895!";
            log.error(errorMessage);
            throw new InvalidValidationExeption(errorMessage);
        }

        if (method == RequestMethod.PUT && film.getId() <= 0) {

            errorMessage = "Фильм не найден, указан некорректный id";
            log.error(errorMessage);
            throw new FilmNotFoundException(errorMessage);
        }
    }
}
