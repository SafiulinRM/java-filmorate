package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    Map<Integer, User> getUsers();

    User getUser(int id);

    Collection<User> findAllUsers();

    User postUser(User user);

    User putUser(User user);

    void removeUser(int id);
}
