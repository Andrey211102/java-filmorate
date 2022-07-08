package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserStorage storage;
    private final UserService service;

    @Autowired
    public UserController(InMemoryUserStorage storage, UserService service) {
        this.storage = storage;
        this.service = service;
    }

    @GetMapping
    public List<User> getAll() {
        return storage.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User newUser) {
        return storage.create(newUser);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return storage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable long id, @PathVariable long friendId) {
        service.addToFriends(id,friendId);
    }

    @DeleteMapping ("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable long id, @PathVariable long friendId) {
        service.removeFromFriends(id,friendId);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable long id){
        return service.get(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable long id){
        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable long id, @PathVariable long otherId){
        return service.getMutualFriends(id, otherId);
    }
}





