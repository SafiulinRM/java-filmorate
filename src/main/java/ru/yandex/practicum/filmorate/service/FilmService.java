package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    @Autowired
    private FilmStorage storage;

    public Film save(Film film) {
        return storage.save(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public List<Film> findAllFilms() {
        return storage.getAll();
    }

    public Film get(long id) {
        return storage.getById(id);
    }

    public void addLike(int id, int userId) {
        storage.addLike(id, userId);
    }

    public void removeLike(int id, int userId) {
        storage.removeLike(id, userId);
    }

    public List<Film> getListOfBestFilms(int count) {
        return storage.getListOfBestFilms(count);
    }
}
