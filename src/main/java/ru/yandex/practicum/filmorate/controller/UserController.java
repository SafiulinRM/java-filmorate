package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<String, User> users = new HashMap<>();

    public void removeUsers(){
        users.clear();
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        try {
            if (users.containsKey(user.getEmail())) {
                throw new ValidationException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }
            checkException(user);
            users.put(user.getEmail(), user);
            log.info("{}", user);
            return user;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            e.getMessage();
            return null;
        }
    }

    @PutMapping
    public User put(@RequestBody User user) {
        try {
            checkException(user);
            users.put(user.getEmail(), user);
            log.info("{}", user);
            return user;
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            e.getMessage();
            return null;
        }
    }

    private void checkException(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthdate().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем.");
        }
    }
}
