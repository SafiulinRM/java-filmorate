package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
    private Map<Integer, User> users = new HashMap<>();
    private int count = 0;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getEmail()) && checkUser(user)) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        checkException(user);
        user.setId(++count);
        users.put(user.getId(), user);
        log.info("{}", user);
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            if (users.get(user.getEmail()).getId() != user.getId()) {
                throw new ValidationException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован под другим id.");
            }
        }
        if (user.getId() == 0) {
            user.setId(++count);
        }
        checkException(user);
        users.put(user.getId(), user);
        log.info("{}", user);
        return user;
    }

    private void checkException(User user) throws ValidationException {
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail() == null || !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем.");
        }
    }

    private boolean checkUser(User user) {
        boolean emailSame = false;
        for (User u : users.values()) {
            if (user.getEmail().equals(u.getEmail())) {
                emailSame = true;
            }
        }
        return emailSame;
    }
}
