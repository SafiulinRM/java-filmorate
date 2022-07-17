package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
public class GenreService {
    @Autowired
    GenreStorage storage;

    public List<Genre> findAllGenres() {
        return storage.getAll();
    }

    public Genre get(int id) {
        return storage.getById(id);
    }
}
