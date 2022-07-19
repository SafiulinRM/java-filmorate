package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpaRating.MpaRatingStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final MpaRatingStorage mpaRatingStorage;
    private final FilmStorage filmStorage;
    private final LocalDate birthday = LocalDate.of(1996, 1, 1);
    private final String email = "qwerty@gmail.com";
    private final String email2 = "update@gmail.com";
    private final String email3 = "test@gmail.com";
    private final String login = "qwerty";
    private final String name = "qwerty123";
    private final String description = "qwertyqwertyqwerty";
    private final int duration = 1000;
    private final String bigDescription = "The film is set in Middle-earth – a land where such “goodly” races as hobbits, elves, dwarfs and men live. Since the ancient times they" +
            " have warred with orcs, goblins and trolls. At the beginning of the film we learn about the One Ring – a powerful weapon created by the Dark Lord Sauron – which" +
            " was occasionally found by Bilbo Baggins, the hobbit. On his 111th birthday Bilbo had a great party, after which he suddenly departed and left his young cousin" +
            " Frodo all his belongings, including the magic ring. A few years later Gandalf, the wizard, visits Frodo to tell him the truth about the ring. A short time after" +
            " that Frodo and three of his friends leave Shire. They are followed by the Black Riders, who are searching for Frodo and the Ring. On their way they meet a new" +
            " friend Aragorn. The Black Riders attacked Frodo and his friends and one of them stabbed Frodo in the shoulder with a cursed knife. Aragorn took Frodo and the" +
            " hobbits to the Rivendell where his wound was healed and the Fellowship of the Ring was formed to take the One Ring to Mordor and destroy it.";
    private final JdbcTemplate jdbcTemplate;
    private final MpaRating mpaRating = new MpaRating(2, "PG");
    private final Genre drama = new Genre(2, "Драма");
    private final List<Genre> genres = List.of(drama);


    @BeforeEach
    public void beforeEach() {
        String sqlQuery = "DELETE FROM FRIENDSHIPS;DELETE FROM FILM_GENRES; DELETE FROM LIKES; DELETE FROM USERS; DELETE FROM FILMS;" +
                " ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;\n" +
                "ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1;\n";
        jdbcTemplate.update(sqlQuery);
    }


    @Test
    public void testGetUserByIdAndCreate() {
        User testUser = new User(email, login, name, birthday);
        userStorage.save(testUser);
        User user = userStorage.getUserById(1);
        assertEquals(1, user.getId(), "Юзер не создался!");
    }

    @Test
    public void testUpdateUser() {
        User testUser = new User(email, login, name, birthday);
        userStorage.save(testUser);
        User updateUser = new User(1, email2, login, name, birthday);
        userStorage.update(updateUser);
        List<User> users = userStorage.getAllUsers();
        assertEquals(1, users.size());
        assertEquals(email2, users.get(0).getEmail(), "Юзер не обновился!");

    }

    @Test
    public void testGetAllUsers() {
        User testUser = new User(email, login, name, birthday);
        userStorage.save(testUser);
        List<User> users = userStorage.getAllUsers();
        assertEquals(testUser, users.get(0), "Не удалось получить всех юзеров!");
    }

    @Test
    public void testAddFriendAndGetFriends() {
        User testUser = new User(email, login, name, birthday);
        userStorage.save(testUser);
        User testUser2 = new User(email2, login, name, birthday);
        userStorage.save(testUser2);
        userStorage.addFriend(1, 2);
        assertEquals(testUser2, userStorage.getFriends(1).get(0), "Не удалось получить друзей!");
    }

    @Test
    public void testRemoveFriends() {
        User testUser = new User(email, login, name, birthday);
        userStorage.save(testUser);
        User testUser2 = new User(email2, login, name, birthday);
        userStorage.save(testUser2);
        userStorage.addFriend(1, 2);
        assertEquals(testUser2, userStorage.getFriends(1).get(0));
        userStorage.removeFriend(1, 2);
        assertEquals(0, userStorage.getFriends(1).size(), "Друг не удалился");
    }

    @Test
    public void testGetListOfMutualFriends() {
        User testUser = new User(email, login, name, birthday);
        userStorage.save(testUser);
        User testUser2 = new User(email2, login, name, birthday);
        userStorage.save(testUser2);
        User testUser3 = new User(email3, login, name, birthday);
        userStorage.save(testUser3);
        userStorage.addFriend(1, 3);
        userStorage.addFriend(2, 3);
        assertEquals(testUser3, userStorage.getListOfMutualFriends(1, 2).get(0), " Не получилось найти общих друзей!");
    }

    @Test
    public void testGetByIdGenre() {
        assertEquals(drama, genreStorage.getById(2), "Не удалось получить жанр");
    }

    @Test
    public void testGetAllGenres() {
        assertEquals(6, genreStorage.getAll().size(), "Не удалось получить жанры");
    }

    @Test
    public void testGetByIdMpa() {
        assertEquals(mpaRating, mpaRatingStorage.getById(2), "Не удалось получить mpa");
    }

    @Test
    public void testGetAllMpa() {
        assertEquals(5, mpaRatingStorage.getAll().size(), "Не удалось получить mpa рейтинги");
    }

    @Test
    public void testGetFilmByIdAndCreate() {
        Film testFilm = new Film(name, description, birthday, duration, mpaRating, genres);
        filmStorage.save(testFilm);
        Film film = filmStorage.getById(1);
        assertEquals(1, film.getId(), "Фильм не создался!");
    }

    @Test
    public void testUpdateFilm() {
        Film testFilm = new Film(name, description, birthday, duration, mpaRating, genres);
        filmStorage.save(testFilm);
        Film updateFilm = new Film(1, login, description, birthday, duration, mpaRating, genres);
        filmStorage.update(updateFilm);
        List<Film> films = filmStorage.getAll();
        assertEquals(1, films.size());
        assertEquals(login, films.get(0).getName(), "Фильм не обновился!");
    }

    @Test
    public void testGetAllFilms() {
        Film testFilm = new Film(name, description, birthday, duration, mpaRating, genres);
        filmStorage.save(testFilm);
        Film updateFilm = new Film(1, login, description, birthday, duration, mpaRating, genres);
        List<Film> films = filmStorage.getAll();
        assertEquals(testFilm, films.get(0), "Не удалось получить все фильмы!");
    }

    @Test
    public void testAddLikeAndGetListOfBestFilms() {
        Film testFilm = new Film(name, description, birthday, duration, mpaRating, genres);
        filmStorage.save(testFilm);
        Film testFilm2 = new Film(login, description, birthday, duration, mpaRating, genres);
        filmStorage.save(testFilm2);
        User testUser = new User(email, login, name, birthday);
        userStorage.save(testUser);
        User testUser2 = new User(email2, login, name, birthday);
        userStorage.save(testUser2);
        filmStorage.addLike(testFilm.getId(), testUser.getId());
        filmStorage.addLike(testFilm.getId(), testUser2.getId());
        filmStorage.addLike(testFilm2.getId(), testFilm.getId());
        List<Film> bestFilms = filmStorage.getListOfBestFilms(7);
        assertEquals(2, bestFilms.size(), "Не удалось получить список лучших фильмов!");
        assertEquals(testFilm, bestFilms.get(0), "Не удалось получить лучший фильм!");
    }

    @Test
    public void testRemoveLike() {
        Film testFilm = new Film(name, description, birthday, duration, mpaRating, genres);
        filmStorage.save(testFilm);
        Film testFilm2 = new Film(login, description, birthday, duration, mpaRating, genres);
        filmStorage.save(testFilm2);
        User testUser = new User(email, login, name, birthday);
        userStorage.save(testUser);
        User testUser2 = new User(email2, login, name, birthday);
        userStorage.save(testUser2);
        filmStorage.addLike(testFilm.getId(), testUser.getId());
        filmStorage.addLike(testFilm.getId(), testUser2.getId());
        filmStorage.addLike(testFilm2.getId(), testFilm.getId());
        List<Film> bestFilms = filmStorage.getListOfBestFilms(7);
        assertEquals(testFilm, bestFilms.get(0), "Не удалось получить лучший фильм!");
        filmStorage.removeLike(testFilm.getId(), testUser.getId());
        filmStorage.removeLike(testFilm.getId(), testUser2.getId());
        List<Film> bestFilms2 = filmStorage.getListOfBestFilms(7);
        assertEquals(testFilm2, bestFilms2.get(0), "Не удалось удалить лайки!");
    }
} 