package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User getUserById(long id);

    List<User> getAllUsers();

    User save(User data);

    User update(User data);

    void addFriend(long userId, long friend);

    List<User> getFriends(long id);

    void removeFriend(long id, long friendId);

    List<User> getListOfMutualFriends(long id, long otherId);
}
