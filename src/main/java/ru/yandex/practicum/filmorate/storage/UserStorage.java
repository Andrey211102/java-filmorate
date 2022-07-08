package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User newUser);
    User update(User user);
    User getUserById(long id);
    void delete(User user);

    List<User> getAll();
}
