package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    public static final int NOT_CREATE_ID = -1;
    private final Map<Integer, User> users = new HashMap<>();
    @Autowired
    private IdGenerator generator;


    public User getUser(int id) {
        return users.get(id);
    }

    public Map<Integer, User> getUsers() {
        return users;
    }

    public Collection<User> findAllUsers() {
        return users.values();
    }

    public User postUser(User user) {
        user.setId(generator.generateUserId());
        users.put(user.getId(), user);
        return user;
    }

    public User putUser(User user) {
        if (user.getId() == NOT_CREATE_ID) {
            user.setId(generator.generateUserId());
        }
        users.put(user.getId(), user);
        return user;
    }

    public void removeUser(int id) {
        users.remove(id);
    }
}
