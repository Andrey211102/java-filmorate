package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage storage;

    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addToFriends(long id, long friendId) {

        User user   = storage.getUserById(id);
        User friend = storage.getUserById(friendId);

        user.addFriend(friendId);
        friend.addFriend(id);
    }

    public void removeFromFriends(long id, long friendId) {

        storage.getUserById(id).removeFriend(friendId);
    }

    public List<User> getFriends(long id){

       return storage.getUserById(id)
               .getFriends()
               .stream()
               .map(idFriend -> storage.getUserById(idFriend))
               .collect(Collectors.toList());
    }

    public List<User> getMutualFriends(long id,long otherId){

        Set<Long> otherFriends = storage.getUserById(otherId).getFriends();
        List<User> mutualFriends = new ArrayList<>();

        for (long idFriend:storage.getUserById(id).getFriends()) {
            if (otherFriends.contains(idFriend))mutualFriends.add(storage.getUserById(idFriend));
        }
 
        return mutualFriends;
    }

    public User get(long id) {
        return storage.getUserById(id);
    }
}

