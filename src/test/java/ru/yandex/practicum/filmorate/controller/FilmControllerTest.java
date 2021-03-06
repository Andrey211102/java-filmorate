package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exeptions.InvalidValidationExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    static Validator validator;
    FilmController controller;

    @BeforeAll
    static void BeforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        InMemoryFilmStorage storage = new InMemoryFilmStorage();
        controller = new FilmController(storage, new FilmService(storage));
    }

    @Test
    void shouldReturnEqualsCreateNewFilm() {

        Film film = new Film("Название", "Описание", LocalDate.of(2020, 1, 21), 200);
        Film newFilm = controller.create(film);

        assertEquals(film,newFilm);
    }

    //name
    @Test
    void shouldNotValidateBlankName(){

        Film film = new Film(null, "Описание", LocalDate.of(2020, 1, 21), 200);
        Film filmSecond = new Film("", "Описание", LocalDate.of(2020, 1, 21), 200);

        assertAll(String.valueOf(true),
                () -> assertFalse(validator.validate(film).isEmpty()),
                () -> assertFalse(validator.validate(filmSecond).isEmpty()));
    }

    //description
    @Test
    void shouldNotValidateBlankDescription(){

        Film film = new Film("Название", null, LocalDate.of(2020, 1, 21), 200);
        Film filmSecond = new Film("Название", " ", LocalDate.of(2020, 1, 21), 200);

        assertAll(String.valueOf(true),
                () -> assertFalse(validator.validate(film).isEmpty()),
                () -> assertFalse(validator.validate(filmSecond).isEmpty()));
    }

    @Test
    void shouldThrowDescriptionLenghtMore200Symbols(){

        String description = "By default, the Persistence provider will automatically perform validation on entities " +
                "with persistent fields or properties annotated with Bean Validation constraints immediately after " +
                "the PrePersist, PreUpdate, and PreRemove lifecycle events";

        Film film = new Film("Название", description, LocalDate.of(2020, 1, 21), 200);

        assertThrows(InvalidValidationExeption.class, () -> controller.create(film));
    }

    //releaseDate
    @Test
    void shouldThrowWhenReleaseDateIsBefore(){
        //28.12.1895
        Film film = new Film("Название", "Описание", LocalDate.of(1895, 12, 27), 200);
        assertThrows(InvalidValidationExeption.class,()-> controller.create(film));
    }

    @Test
    void shouldDoesNotThrowWhenReleaseDateIsAfter(){
        //28.12.1895
        Film film = new Film("Название", "Описание", LocalDate.of(1895, 12, 29), 200);

        assertDoesNotThrow(()-> controller.create(film));
    }

    //duration
    @Test
    void shouldThrowWhenDurationIncorrect(){

        Film film = new Film("Название", "Описание", LocalDate.of(1895, 12, 27), 0);
        Film filmSecod = new Film("Название", "Описание", LocalDate.of(1895, 12, 27), -1);

        assertAll(String.valueOf(true),
                () -> assertThrows(InvalidValidationExeption.class,()-> controller.create(film)),
                () -> assertThrows(InvalidValidationExeption.class,()-> controller.create(filmSecod)));
    }

    //PUT
    @Test
    void shouldDoesNotThrowWhenCorrectIdPUT(){

        Film film = new Film("Название", "Описание", LocalDate.of(2022, 12, 29), 200);
        Film savedFilm = controller.create(film);

        Film updateFilm = new Film("Обновленное название", "Обновленное описание", LocalDate.of(2022, 12, 29), 200);
        updateFilm.setId(savedFilm.getId());

        assertDoesNotThrow(()-> controller.update(updateFilm));
    }

    @Test
    void shouldThrowWhenIncorrectIdPUT(){

        Film film = new Film("Название", "Описание", LocalDate.of(2022, 12, 29), 200);

        Film updateFilm = new Film("Обновленное название", "Обновленное описание", LocalDate.of(2022, 12, 29), 200);
        updateFilm.setId(-2);

        assertThrows(InvalidValidationExeption.class,()-> controller.update(updateFilm));
    }

    //GET films

    @Test
    void shouldEqualsFilmsListGET(){

        Film film = new Film("Название", "Описание", LocalDate.of(2022, 12, 29), 200);
        Film filmSecond = new Film("Название2", "Описание2", LocalDate.of(2019, 11, 19), 210);
        Film filmThird = new Film("Название3", "Описание3", LocalDate.of(2013, 12, 22), 110);

        controller.create(film);
        controller.create(filmSecond);
        controller.create(filmThird);

        List<Film> films = controller.getAll();

        assertAll(String.valueOf(true),
                () -> assertEquals(3,films.size()),
                () -> assertEquals(film,films.get(0)),
                () -> assertEquals(filmSecond,films.get(1)),
                () -> assertEquals(filmThird,films.get(2)));
    }
}