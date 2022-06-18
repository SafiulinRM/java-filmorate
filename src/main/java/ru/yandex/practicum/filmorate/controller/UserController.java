package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable int id) {
        if (!userService.getUserStorage().getUsers().containsKey(id)) {
            throw new NullPointerException("Пользователь отсутствует с id: " + id);
        }
        log.info("Получен пользователь по id: {}", id);
        return userService.getUserStorage().getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователю с id: {} добавлен друг с id: {}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.removeFriend(id, friendId);
        log.info("Пользователь с id: {} удалил друга с id: {}", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getListOfFriends(@PathVariable int id) {
        List<User> friends = userService.getListOfFriends(id);
        log.info("Текущее количество друзей: {}", friends.size());
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getListOfMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        List<User> friends = userService.getListOfMutualFriends(id, otherId);
        log.info("Текущее количество общих друзей: {}", friends.size());
        return friends;
    }

    @GetMapping
    public Collection<User> findAll() {
        Collection<User> users = userService.getUserStorage().findAllUsers();
        log.info("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (isUserEmailUnique(user) && user.getId() != 0) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        validateUser(user);
        userService.getUserStorage().postUser(user);
        log.info("Пользователь создан: {}", user);
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        Map<Integer, User> users = userService.getUserStorage().getUsers();
        if (!users.containsKey(user.getId()) && isUserEmailUnique(user)) {
            throw new NullPointerException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован под другим id.");
        }
        validateUser(user);
        userService.getUserStorage().putUser(user);
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
        Map<Integer, User> users = userService.getUserStorage().getUsers();
        boolean emailSame = false;
        for (User u : users.values()) {
            if (user.getEmail().equals(u.getEmail())) {
                emailSame = true;
                break;
            }
        }
        return emailSame;
    }
}
