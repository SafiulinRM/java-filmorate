package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    public static int DEFAULT_FILMS_COUNT = 10;
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    FilmService service;

    @PostMapping
    public Film create(@RequestBody final Film film) {
        validateFilm(film);
        log.info("Creating film {}", film);
        return service.save(film);
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        if (film.getId() < 1) {
            log.warn("Id не может быть меньше 1, текущий: {}", film.getId());
            throw new NotFoundException("Id не может быть меньше 1, текущий: " + film.getId());
        }
        validateFilm(film);
        service.update(film);
        log.info("Фильм создан или изменен: {}", film);
        return service.update(film);
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable long id) {
        log.info("Get film id={}", id);
        return service.get(id);
    }

    @GetMapping
    public List<Film> findAllUsers() {
        List<Film> films = service.findAllFilms();
        log.info("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @GetMapping("/popular")
    public List<Film> getListOfBestFilms(@RequestParam(required = false) String count) {
        int filmCount = count != null ? Integer.parseInt(count) : DEFAULT_FILMS_COUNT;
        log.info("список из первых {} фильмов по количеству лайков", filmCount);
        return service.getListOfBestFilms(filmCount);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        service.addLike(id, userId);
        log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        if (id < 1 || userId < 1) {
            log.warn("Id не может быть меньше 1, текущий: {}, userId: {}", id, userId);
            throw new NotFoundException("Id не может быть меньше 1, текущий: " + id + ",userId: " + userId);
        }
        service.removeLike(id, userId);
        log.info("Пользователь с id: {} удалил лайк фильму с id: {}", userId, id);
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("название не может быть пустым.");
            throw new ValidationException("название не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания — 200 символов, текущая: {}", film.getDescription().length());
            throw new ValidationException("Максимальная длина описания — 200 символов, текущая: " + film.getDescription().length());
        }
        if (EARLIEST_RELEASE_DATE.isAfter(film.getReleaseDate())) {
            log.warn("Дата релиза — не раньше 28 декабря 1895 года, текущая: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года, текущая: " + film.getReleaseDate());
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть больше 0, текущая: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть больше 0, текущая: " + film.getDuration());
        }
    }
}
