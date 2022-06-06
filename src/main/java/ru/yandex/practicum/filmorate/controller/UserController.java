package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.InvalidValidationExeption;
import ru.yandex.practicum.filmorate.model.User;

import lombok.extern.slf4j.Slf4j;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private Long lastUid;
    private final HashMap<Long, User> users;

    public UserController() {
        this.users = new HashMap<>();
        this.lastUid = 1L;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(this.users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User newUser) {

        validate(newUser, RequestMethod.POST);

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
    public User update(@Valid @RequestBody User user) {

        validate(user, RequestMethod.PUT);

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

    private void validate(User user, RequestMethod method) {

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

