package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private IdGenerator generator = new IdGenerator();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (users.containsKey(user.getEmail()) && isUserEmailUnique(user)) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        validateUser(user);
        user.setId(generator.generate());
        users.put(user.getId(), user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            if (users.get(user.getEmail()).getId() != user.getId()) {
                throw new ValidationException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован под другим id.");
            }
        }
        if (user.getId() == 0) {
            user.setId(generator.generate());
        }
        validateUser(user);
        users.put(user.getId(), user);
        log.info("Пользователь создан или изменен: {}", user);
        return user;
    }

    private void validateUser(User user) {
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.warn("Логин не может быть пустым и содержать пробелы, текущий: {}", user.getLogin());
            throw new ValidationException("Логин не может быть пустым и содержать пробелы, текущий: " + user.getLogin());
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            log.warn("Электронная почта не может быть пустой и должна содержать символ @, текущая: {}", user.getEmail());
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @, текущая: " + user.getEmail());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем, текущая: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем, текущая: " + user.getBirthday());
        }
    }

    private boolean isUserEmailUnique(User user) {
        boolean emailSame = false;
        for (User u : users.values()) {
            if (user.getEmail().equals(u.getEmail())) {
                emailSame = true;
            }
        }
        return emailSame;
    }
}
