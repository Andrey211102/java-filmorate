package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public User create(User newUser);
    public User update(User user);
    public User getUserById(long id);

    public List<User> getAll();
}
