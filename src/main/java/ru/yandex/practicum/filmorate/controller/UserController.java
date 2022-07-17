package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService service;

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        if (id < 1 || friendId < 1) {
            log.warn("Id не может быть меньше 1, текущий id: {}, friendId: {}", id, friendId);
            throw new NotFoundException("Id не может быть меньше 1, текущий: " + id + " friendId: " + friendId);
        }
        service.addFriend(id, friendId);
        log.info("Пользователю с id: {} добавлен друг с id: {}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        service.removeFriend(id, friendId);
        log.info("Пользователь с id: {} удалил друга с id: {}", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getListOfMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        List<User> friends = service.getListOfMutualFriends(id, otherId);
        log.info("Текущее количество общих друзей: {}", friends.size());
        return friends;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validateUser(user);
        log.info("Creating user {}", user);
        return service.save(user);
    }

    @PutMapping
    public User put(@RequestBody User user) {
        if (user.getId() < 1) {
            log.warn("Id не может быть меньше 1, текущий: {}", user.getId());
            throw new NotFoundException("Id не может быть меньше 1, текущий: " + user.getId());
        }
        validateUser(user);
        service.update(user);
        log.info("Пользователь создан или изменен: {}", user);
        return user;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        if (id < 1) {
            log.warn("Id не может быть меньше 1, текущий: {}", id);
            throw new NotFoundException("Id не может быть меньше 1, текущий: " + id);
        }
        log.info("Get user id={}", id);
        return service.get(id);
    }

    @GetMapping
    public List<User> findAllUsers() {
        List<User> users = service.findAllUsers();
        log.info("Текущее количество пользователей: {}", users.size());
        return users;
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
}