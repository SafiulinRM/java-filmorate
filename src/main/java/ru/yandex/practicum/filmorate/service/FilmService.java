package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;


    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film save(Film film) {
        return filmStorage.save(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> findAllFilms() {
        return filmStorage.getAll();
    }

    public Film get(long id) {
        return filmStorage.getById(id);
    }

    public void addLike(int id, int userId) {
        filmStorage.addLike(id, userId);
    }

    public void removeLike(int id, int userId) {
        filmStorage.removeLike(id, userId);
    }

    public List<Film> getListOfBestFilms(int count) {
        return filmStorage.getListOfBestFilms(count);
    }
}
