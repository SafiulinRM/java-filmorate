package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpaRating.MpaRatingStorage;

import java.util.List;

@Service
public class MpaService {
    @Autowired
    MpaRatingStorage storage;

    public List<MpaRating> findAllMpaRatings() {
        return storage.getAll();
    }

    public MpaRating get(int id) {
        return storage.getById(id);
    }
}
