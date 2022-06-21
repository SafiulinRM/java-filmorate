package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

@Service
public class IdGenerator {
    private int userId = 0;
    private int filmId = 0;

    public int generateUserId() {
        return ++userId;
    }

    public int generateFilmId() {
        return ++filmId;
    }
}