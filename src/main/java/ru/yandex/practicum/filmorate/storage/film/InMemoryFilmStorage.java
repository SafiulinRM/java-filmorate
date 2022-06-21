package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    public static final int NOT_CREATE_ID = -1;
    private final Map<Integer, Film> films = new HashMap<>();
    @Autowired
    private IdGenerator generator;

    public Film getFilm(int id) {
        return films.get(id);
    }

    public Map<Integer, Film> getFilms() {
        return films;
    }

    public Film postFilm(Film film) {
        film.setId(generator.generateFilmId());
        films.put(film.getId(), film);
        return film;
    }

    public Film putFilm(Film film) {
        if (film.getId() == NOT_CREATE_ID) {
            film.setId(generator.generateFilmId());
        }
        films.put(film.getId(), film);
        return film;
    }

    public void removeFilm(int id) {
        films.remove(id);
    }
}
