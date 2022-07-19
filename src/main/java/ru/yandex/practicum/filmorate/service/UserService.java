package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long id, long friendId) {
        userStorage.addFriend(id, friendId);
    }

    public List<User> getFriends(long id) {
        return userStorage.getFriends(id);
    }

    public void removeFriend(long id, long friendId) {
        userStorage.removeFriend(id, friendId);
    }

    public List<User> getListOfMutualFriends(long id, long otherId) {
        return userStorage.getListOfMutualFriends(id, otherId);
    }

    public User save(User user) {
        return userStorage.save(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> findAllUsers() {
        return userStorage.getAllUsers();
    }

    public User get(long id) {
        return userStorage.getUserById(id);
    }
}