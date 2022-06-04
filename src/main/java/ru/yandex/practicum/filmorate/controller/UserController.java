package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.InvalidValidationExeption;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private Long lastUid;
    private final HashMap<Long, User> users;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    public UserController() {
        this.users = new HashMap<>();
        this.lastUid = 1L;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(this.users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {

        validateUser(newUser, RequestMethod.POST);

        if (newUser.getName() == null || newUser.getName().equals("")) {
            newUser.setName(newUser.getLogin());
        }

        newUser.setId(this.lastUid);

        users.put(newUser.getId(), newUser);
        log.info("Добавлен пользователь: {}", newUser);

        setLastUid();

        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {

        validateUser(user, RequestMethod.PUT);

        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("Обновлен пользователь: {}", user);

        return user;
    }

    private void setLastUid() {

        if (this.users.isEmpty()) {
            this.lastUid = 1L;
        } else {
            this.lastUid++;
        }
    }

    private void validateUser(User user, RequestMethod method) {

        String errorMessage;

        if (user.getBirthday().isAfter(LocalDate.now())) {

            errorMessage = "Дата рождения не может быть в будущем!";
            log.error(errorMessage);
            throw new InvalidValidationExeption(errorMessage);
        }

        if (method == RequestMethod.PUT && user.getId() <= 0) {

            errorMessage = "Указан некорректный id";
            log.error(errorMessage);
            throw new InvalidValidationExeption(errorMessage);
        }
    }

}

