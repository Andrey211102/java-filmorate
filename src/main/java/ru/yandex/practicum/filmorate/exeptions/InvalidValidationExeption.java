package ru.yandex.practicum.filmorate.exeptions;

public class InvalidValidationExeption extends RuntimeException{
    public InvalidValidationExeption(String message) {
        super(message);
    }
}
