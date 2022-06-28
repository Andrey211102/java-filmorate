package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.yandex.practicum.filmorate.exeptions.InvalidValidationExeption;
import ru.yandex.practicum.filmorate.exeptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private Long lastUid;

    private final HashMap<Long, User> users;

    public InMemoryUserStorage() {
        this.users = new HashMap<>();
        this.lastUid = 1L;
    }

    @Override
    public User create(User newUser)  {
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

    @Override
    public User update(User user) {
        validate(user, RequestMethod.PUT);

        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("Обновлен пользователь: {}", user);

        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(this.users.values());
    }

    @Override
    public User getUserById(long id) {

        if (!this.users.containsKey(id)) {
            throw new UserNotFoundException("Ошибка получения пользователя по id:" + id);
        }

        return this.users.get(id);
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

            errorMessage = "Пользователь не найден, Указан некорректный id";
            log.error(errorMessage);
            throw new UserNotFoundException(errorMessage);
        }
    }

}