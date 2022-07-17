package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    @Autowired
    MpaService service;

    @GetMapping("/{id}")
    public MpaRating get(@PathVariable int id) {
        log.info("Get mpa id={}", id);
        return service.get(id);
    }

    @GetMapping
    public List<MpaRating> findAllMpaRatings() {
        List<MpaRating> mpaRatings = service.findAllMpaRatings();
        log.info("Текущее количество рейтингов: {}", mpaRatings.size());
        return mpaRatings;
    }
}

