package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.InvalidValidationExeption;
import ru.yandex.practicum.filmorate.model.Film;

import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private Long lastUid;
    private final HashMap<Long, Film> films;

    public FilmController() {
        this.films = new HashMap<>();
        this.lastUid = 1L;
    }

    @GetMapping
    public List<Film> get() {
        return new ArrayList<>(this.films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film newFilm) {

        validate(newFilm, RequestMethod.POST);

        newFilm.setId(this.lastUid);

        this.films.put(newFilm.getId(), newFilm);
        log.info("Добавлен фильм: {}", newFilm);

        setLastUid();

        return newFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {

        validate(film, RequestMethod.PUT);

        this.films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);

        return film;
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

            errorMessage = "Указан некорректный id";
            log.error(errorMessage);
            throw new InvalidValidationExeption(errorMessage);
        }
    }
}




