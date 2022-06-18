package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Data
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
            throw new NullPointerException("Отсутствует либо фильм с таким id, либо лайк с userId");
        }
        filmStorage.getFilm(id).removeLike(userId);
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
}
