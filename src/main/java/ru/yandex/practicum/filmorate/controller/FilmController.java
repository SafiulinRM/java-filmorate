package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private IdGenerator generator = new IdGenerator();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        film.setId(generator.generate());
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Фильм создан: {}", film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            if (isFilmNameUnique(film)) {
                throw new ValidationException("Фильм с названием " +
                        film.getName() + " уже зарегистрирован под другим id.");
            }
        }
        if (film.getId() == 0) {
            film.setId(generator.generate());
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Фильм создан или изменен: {}", film);
        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("название не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов, текущая: " + film.getDescription().length());
        }
        if (EARLIEST_RELEASE_DATE.isAfter(film.getReleaseDate())) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года, текущая: " + film.getReleaseDate());
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть больше 0, текущая: " + film.getDuration());
        }
    }

    private boolean isFilmNameUnique(Film film) {
        boolean nameSame = false;
        for (Film f : films.values()) {
            if (film.getName().equals(f.getName())) {
                nameSame = true;
            }
        }
        return nameSame;
    }
}
