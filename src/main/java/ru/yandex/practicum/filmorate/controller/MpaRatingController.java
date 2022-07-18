package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaRatingController {
    private final MpaRatingService mpaRatingService;

    @Autowired
    public MpaRatingController(MpaRatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping("/{id}")
    public MpaRating get(@PathVariable int id) {
        log.info("Get mpa id={}", id);
        return mpaRatingService.get(id);
    }

    @GetMapping
    public List<MpaRating> findAllMpaRatings() {
        List<MpaRating> mpaRatings = mpaRatingService.findAllMpaRatings();
        log.info("Текущее количество рейтингов: {}", mpaRatings.size());
        return mpaRatings;
    }
}

