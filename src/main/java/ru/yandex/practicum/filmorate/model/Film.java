package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeptions.UserNotFoundException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
public class Film {

    private Set<Long> likes = new HashSet<>();

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;

    private long id;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public boolean addLike(long id){
        return this.likes.add(id);
    }

    public boolean removeLike(long id){

        if (!likes.contains(id)){

            String errorMessage = "У фильма " + name + " нет лайка пользователя с id " + id;
            log.error(errorMessage);

            throw new UserNotFoundException(errorMessage);
        }

        return this.likes.remove(id);
    }
}
