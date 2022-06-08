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
    private int count = 0;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        film.setId(++count);
        checkException(film);
        films.put(film.getId(), film);
        log.info("{}", film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            if (checkFilm(film)) {
                throw new ValidationException("Фильм с названием " +
                        film.getName() + " уже зарегистрирован под другим id.");
            }
        }
        if (film.getId() == 0) {
            film.setId(++count);
        }
        checkException(film);
        films.put(film.getId(), film);
        log.info("{}", film);
        return film;
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

    private boolean checkFilm(Film film) {
        boolean nameSame = false;
        for (Film f : films.values()) {
            if (film.getName().equals(f.getName())) {
                nameSame = true;
            }
        }
        return nameSame;
    }
}
