package ru.yandex.practicum.filmorate.service;

public class IdGenerator {
    private int counterId = 0;

    public int generate() {
        return ++counterId;
    }
}