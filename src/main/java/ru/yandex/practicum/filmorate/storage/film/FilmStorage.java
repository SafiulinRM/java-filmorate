package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getById(long id);

    List<Film> getAll();

    Film save(Film data);

    Film update(Film data);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Film> getListOfBestFilms(int count);
}
