package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exeptions.InvalidValidationExeption;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {

    static Validator validator;
    UserController controller;

    @BeforeAll
    static void BeforeAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    void setUp() {
        controller = new UserController();
    }

    @Test
    void shouldReturnEqualsCreateNewUser() {

        User user = new User("ivan@gmail.com", "ivan.I", "Иванов", LocalDate.of(2011, 1, 21));
        User newUser = controller.createUser(user);

        assertEquals(user, newUser);
    }

    //email
    @Test
    void shouldNotValidateIncorrectEmail() {

        User user = new User(null, "ivan.I", "Иванов", LocalDate.of(2011, 1, 21));
        User userSecond = new User("", "Petr.I", "Петров", LocalDate.of(2011, 1, 21));
        User userThrid = new User("sidorgmail.com", "Sidor.I", "Сидоров", LocalDate.of(2011, 1, 21));

        assertAll(String.valueOf(true),
                () -> assertFalse(validator.validate(user).isEmpty()),
                () -> assertFalse(validator.validate(userSecond).isEmpty()),
                () -> assertFalse(validator.validate(userThrid).isEmpty()));
    }

    //birthday
    @Test
    void shouldThrowWhenBirthdayIsAfterNow() {

        User user = new User(null, "ivan.I", "Иванов", LocalDate.of(2023, 1, 21));
        assertThrows(InvalidValidationExeption.class, () -> controller.createUser(user));
    }

    //login
    @Test
    void shouldNotValidateloginBlank() {

        User user = new User("ivan@gmail.com", null, "Иванов", LocalDate.of(2011, 1, 21));
        User userSecond = new User("petroff@ya.ru", "", "Петров", LocalDate.of(2011, 1, 21));
        User userThrid = new User("sidor@mail.com", "   ", "Сидоров", LocalDate.of(2011, 1, 21));

        assertAll(String.valueOf(true),
                () -> assertFalse(validator.validate(user).isEmpty()),
                () -> assertFalse(validator.validate(userSecond).isEmpty()),
                () -> assertFalse(validator.validate(userThrid).isEmpty()));
    }

    //name
    @Test
    void shouldDoesNotThrowNameBlank() {

        User user = new User("ivan@gmail.com", "ivan.P", null, LocalDate.of(2011, 1, 21));
        User userSecond = new User("petroff@ya.ru", "petr.I", "", LocalDate.of(2011, 1, 21));

        assertAll(String.valueOf(true),
                () -> assertDoesNotThrow(() -> controller.createUser(user)),
                () -> assertDoesNotThrow(() -> controller.createUser(userSecond)));
    }

    @Test
    void shouldEqualsLoginNameBlank() {

        User user = new User("ivan@gmail.com", "ivan.P", null, LocalDate.of(2011, 1, 21));
        User userSecond = new User("petroff@ya.ru", "petr.I", "", LocalDate.of(2011, 1, 21));

        User newUser = controller.createUser(user);
        User newUserSecond = controller.createUser(userSecond);

        assertAll(String.valueOf(true),
                () -> assertEquals(newUser.getName(), newUser.getLogin()),
                () -> assertEquals(newUserSecond.getName(), newUserSecond.getLogin()));
    }

    //PUT
    @Test
    void shouldDoesNotThrowWhenCorrectIdPUT() {

        User user = new User("ivan@gmail.com", "ivan.I", "Иванов", LocalDate.of(2011, 1, 21));
        User savedUser = controller.createUser(user);

        User updateUser = new User("petroff@ya.ru", "petr.I", "Петров", LocalDate.of(2012, 11, 22));
        updateUser.setId(savedUser.getId());

        assertDoesNotThrow(() -> controller.updateUser(updateUser));
    }

    @Test
    void shouldThrowWhenIncorrectIdPUT() {

        User user = new User("ivan@gmail.com", "ivan.I", "Иванов", LocalDate.of(2011, 1, 21));
        User savedUser = controller.createUser(user);

        User updateUser = new User("petroff@ya.ru", "petr.I", "Петров", LocalDate.of(2012, 11, 22));
        updateUser.setId(-2);

        assertThrows(InvalidValidationExeption.class, () -> controller.updateUser(updateUser));
    }

    //GET users
    @Test
    void shouldEqualsFilmsListGET() {

        User user = new User("ivan@gmail.com", "ivan.I", "Иванов", LocalDate.of(2012, 1, 21));
        User userSecond = new User("petroff@ya.ru", "Petr.V", "Петров", LocalDate.of(2011, 1, 21));
        User userThrid = new User("sidor@mail.com", "Sidor.I", "Сидоров", LocalDate.of(2010, 1, 21));

        controller.createUser(user);
        controller.createUser(userSecond);
        controller.createUser(userThrid);

        List<User> users = controller.getUsers();

        assertAll(String.valueOf(true),
                () -> assertEquals(3, users.size()),
                () -> assertEquals(user, users.get(0)),
                () -> assertEquals(userSecond, users.get(1)),
                () -> assertEquals(userThrid, users.get(2)));
    }
}


