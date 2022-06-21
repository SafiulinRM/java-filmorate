package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUser(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Пользователь отсутствует с id: " + id);
        }
        return userStorage.getUser(id);
    }

    public Map<Integer, User> getUsers() {
        return userStorage.getUsers();
    }

    public Collection<User> findAllUsers() {
        return userStorage.getUsers().values();
    }

    public User postUser(User user) {
        if (isUserEmailUnique(user) && user.getId() != InMemoryUserStorage.NOT_CREATE_ID) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        return userStorage.postUser(user);
    }

    public User putUser(User user) {
        Map<Integer, User> users = userStorage.getUsers();
        if (!users.containsKey(user.getId()) && isUserEmailUnique(user)) {
            throw new NotFoundException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован под другим id.");
        }
        return userStorage.putUser(user);
    }

    public void addFriend(int id, int friendId) {
        if (!userStorage.getUsers().containsKey(id) || !userStorage.getUsers().containsKey(friendId)) {
            throw new NotFoundException("Такой id отсутствует.");
        }
        userStorage.getUser(id).addFriend(friendId);
        userStorage.getUser(friendId).addFriend(id);
    }

    public void removeFriend(int id, int friendId) {
        userStorage.getUser(id).removeFriend(friendId);
        userStorage.getUser(friendId).removeFriend(id);
    }

    public List<User> getListOfFriends(int id) {
        Set<Integer> friendIds = userStorage.getUser(id).getFriends();
        List<User> friends = new ArrayList<>();
        for (Integer friendId : friendIds) {
            friends.add(userStorage.getUser(friendId));
        }
        return friends;
    }

    public List<User> getListOfMutualFriends(int id, int otherId) {
        List<User> friends = new ArrayList<>();
        if (userStorage.getUsers().containsKey(id)) {
            Set<Integer> friendIds = new HashSet<>(userStorage.getUser(id).getFriends());
            Set<Integer> otherFriendIds = new HashSet<>(userStorage.getUser(otherId).getFriends());

            if (!friendIds.isEmpty() && !otherFriendIds.isEmpty()) {
                friendIds.retainAll(otherFriendIds);
                for (Integer friendId : friendIds) {
                    friends.add(userStorage.getUser(friendId));
                }
            }
        }
        return friends;
    }

    private boolean isUserEmailUnique(User user) {
        Map<Integer, User> users = userStorage.getUsers();
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
