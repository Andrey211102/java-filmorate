package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;

@Service
public class FilmService {

    private final FilmStorage storage;

    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public void addLike(long id, long userId) {

        Film film = storage.getFilmById(id);

        storage.getFilmById(id).addLike(userId);
    }

    public void removeLike(long id, long userId) {

        storage.getFilmById(id).removeLike(userId);
    }

    public List<Film> getPopular(Integer count) {

        count = count == null ? 10 : count;
        count = count == 0 ? 10 : count;

        return storage.getAll()
                .stream()
                .sorted((f0, f1) -> compare(f1.getLikes().size(), f0.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}


