package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private IdGenerator generator = new IdGenerator();

    public Film getFilm(int id) {
        return films.get(id);
    }

    public Collection<Film> findAllFilms() {
        return films.values();
    }

    public Map<Integer, Film> getFilms() {
        return films;
    }

    public Film postFilm(Film film) {
        film.setId(generator.generate());
        films.put(film.getId(), film);
        return film;
    }

    public Film putFilm(Film film) {
        if (film.getId() == 0) {
            film.setId(generator.generate());
        }
        films.put(film.getId(), film);
        return film;
    }

    public void removeFilm(int id) {
        films.remove(id);
    }
}
