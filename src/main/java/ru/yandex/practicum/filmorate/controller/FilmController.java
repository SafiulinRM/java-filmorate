package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final LocalDate movieBirthday = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        try {
            checkException(film);
            films.put(film.getId(), film);
            log.info("{}", film);
            return film;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            e.getMessage();
            return null;
        }
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        try {
            checkException(film);
            films.put(film.getId(), film);
            log.info("{}", film);
            return film;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            e.getMessage();
            return null;
        }
    }

    private void checkException(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("название не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов.");
        }
        if (movieBirthday.isAfter(film.getReleaseDate())) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной.");
        }
    }
}
