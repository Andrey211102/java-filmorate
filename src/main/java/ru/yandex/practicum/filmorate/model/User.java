package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeptions.UserNotFoundException;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
public class User {

    @Email
    @NotBlank
    private String email;
    private Set<Long> friends = new HashSet<>();

    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login;

    private long id;
    private String name;
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public boolean addFriend(long id){
        return this.friends.add(id);
    }

    public boolean removeFriend(long id){

        if (!friends.contains(id)){

            String errorMessage = "В друзьях пользователя " + name + " нет пользователя с id " + id;
            log.error(errorMessage);

            throw new UserNotFoundException(errorMessage);
        }

        return this.friends.remove(id);
    }
}

