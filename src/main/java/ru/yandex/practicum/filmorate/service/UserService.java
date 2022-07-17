package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserStorage repository;

    public void addFriend(long id, long friendId) {
        repository.addFriend(id, friendId);
    }

    public List<User> getFriends(long id) {
        return repository.getFriends(id);
    }

    public void removeFriend(long id, long friendId) {
        repository.removeFriend(id, friendId);
    }

    public List<User> getListOfMutualFriends(long id, long otherId) {
        return repository.getListOfMutualFriends(id, otherId);
    }

    public User save(User user) {
        return repository.save(user);
    }

    public User update(User user) {
        return repository.update(user);
    }

    public List<User> findAllUsers() {
        return repository.getAllUsers();
    }

    public User get(long id) {
        return repository.getUserById(id);
    }
}