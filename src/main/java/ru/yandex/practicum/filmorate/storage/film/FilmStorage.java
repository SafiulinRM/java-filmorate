package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    Map<Integer, Film> getFilms();

    Film getFilm(int id);

    Film postFilm(Film film);

    Film putFilm(Film film);

    void removeFilm(int id);
}
