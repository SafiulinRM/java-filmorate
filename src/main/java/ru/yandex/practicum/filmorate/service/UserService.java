package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int id, int friendId) {
        if (!userStorage.getUsers().containsKey(id) || !userStorage.getUsers().containsKey(friendId)) {
            throw new NullPointerException("Такой id отсутствует.");
        }
        userStorage.getUser(id).addFriend(friendId);
        userStorage.getUser(friendId).addFriend(id);
    }

    public void removeFriend(int id, int friendId) {
        userStorage.getUser(id).removeFriend(friendId);
        userStorage.getUser(friendId).removeFriend(id);
    }

    public List<User> getListOfFriends(int id) {
        Set<Integer> friendsId = getUserStorage().getUser(id).getFriends();
        List<User> friends = new ArrayList<>();
        for (Integer friendId : friendsId) {
            friends.add(userStorage.getUser(friendId));
        }
        return friends;
    }

    public List<User> getListOfMutualFriends(int id, int otherId) {
        List<User> friends = new ArrayList<>();
        if (userStorage.getUsers().containsKey(id)) {
            Set<Integer> man = new HashSet<>(getUserStorage().getUser(id).getFriends());
            Set<Integer> otherMan = new HashSet<>(getUserStorage().getUser(otherId).getFriends());

            if (!man.isEmpty() && !otherMan.isEmpty()) {
                man.retainAll(otherMan);
                for (Integer friendId : man) {
                    friends.add(userStorage.getUser(friendId));
                }
            }
        }
        return friends;
    }
}
