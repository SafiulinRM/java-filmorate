package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final int DEFAULT_FILMS_COUNT = 10;
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable int id) {
        log.info("Получен фильм по id: {}", id);
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
        log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        filmService.removeLike(id, userId);
        log.info("Пользователь с id: {} удалил лайк фильму с id: {}", userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getListOfBestFilms(@RequestParam(required = false) String count) {
        int filmCount = count != null ? Integer.parseInt(count) : DEFAULT_FILMS_COUNT;
        log.info("список из первых {} фильмов по количеству лайков", filmCount);
        return filmService.getListOfBestFilms(filmCount);
    }

    @GetMapping
    public Collection<Film> findAll() {
        Collection<Film> films = filmService.findAllFilms();
        log.info("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        validateFilm(film);
        filmService.postFilm(film);
        log.info("Фильм создан: {}", film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        validateFilm(film);
        filmService.putFilm(film);
        log.info("Фильм создан или изменен: {}", film);
        return film;
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
