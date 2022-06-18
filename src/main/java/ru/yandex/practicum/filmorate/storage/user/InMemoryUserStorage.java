package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private IdGenerator generator = new IdGenerator();

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
        user.setId(generator.generate());
        users.put(user.getId(), user);
        return user;
    }

    public User putUser(User user) {
        if (user.getId() == 0) {
            user.setId(generator.generate());
        }
        users.put(user.getId(), user);
        return user;
    }

    public void removeUser(int id) {
        users.remove(id);
    }
}
