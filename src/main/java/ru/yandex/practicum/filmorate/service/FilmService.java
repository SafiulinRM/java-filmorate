package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int id, int userId) {
        filmStorage.getFilm(id).addLike(userId);
    }

    public void removeLike(int id, int userId) {
        if (!filmStorage.getFilms().containsKey(id) || !filmStorage.getFilm(id).getLikes().contains(userId)) {
            throw new NotFoundException("Отсутствует либо фильм с таким id, либо лайк с userId");
        }
        filmStorage.getFilm(id).removeLike(userId);
    }

    public Film getFilm(int id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Фильм отсутствует с id: " + id);
        }
        return filmStorage.getFilm(id);
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.getFilms().values();
    }

    public Film postFilm(Film film) {
        return filmStorage.postFilm(film);
    }

    public Film putFilm(Film film) {
        Map<Integer, Film> films = filmStorage.getFilms();
        if (!films.containsKey(film.getId()) && isFilmNameUnique(film)) {
            throw new NotFoundException("Фильм с названием " +
                    film.getName() + " уже зарегистрирован под другим id.");
        }
        return filmStorage.putFilm(film);
    }

    public List<Film> getListOfBestFilms(int count) {
        Map<Integer, Film> films = filmStorage.getFilms();
        Set<Film> prioritizedFilms = new TreeSet<>(films.values());
        if (count > prioritizedFilms.size()) {
            count = prioritizedFilms.size();
        }
        List<Film> bestFilms = new ArrayList<>();
        int sizeList = 0;
        for (Film film : prioritizedFilms) {
            if (sizeList < count) {
                sizeList++;
                bestFilms.add(film);
            } else {
                break;
            }
        }
        return bestFilms;
    }

    private boolean isFilmNameUnique(Film film) {
        Map<Integer, Film> films = filmStorage.getFilms();
        boolean nameSame = false;
        for (Film f : films.values()) {
            if (film.getName().equals(f.getName())) {
                nameSame = true;
            }
        }
        return nameSame;
    }
}
