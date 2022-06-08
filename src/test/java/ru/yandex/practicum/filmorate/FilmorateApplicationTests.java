package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
class FilmorateApplicationTests {
    public static String USER_NOT_CREATE = "пользователь не создался";
    public static String FILM_NOT_CREATE = "фильм не создался";
    public static String EXCEPTION = "Тест на пользователя не пройден";
    public static String EXCEPTION_FILM = "Тест на фильм не пройден";
    private final LocalDate birthdate = LocalDate.of(1996, 1, 1);
    private final String email = "qwerty@gmail.com";
    private final String login = "qwerty";
    private final String name = "qwerty123";
    private final String description = "qwertyqwertyqwerty";
    private final long duration = 1000;
    private final String bigDescription = "The film is set in Middle-earth – a land where such “goodly” races as hobbits, elves, dwarfs and men live. Since the ancient times they" +
            " have warred with orcs, goblins and trolls. At the beginning of the film we learn about the One Ring – a powerful weapon created by the Dark Lord Sauron – which" +
            " was occasionally found by Bilbo Baggins, the hobbit. On his 111th birthday Bilbo had a great party, after which he suddenly departed and left his young cousin" +
            " Frodo all his belongings, including the magic ring. A few years later Gandalf, the wizard, visits Frodo to tell him the truth about the ring. A short time after" +
            " that Frodo and three of his friends leave Shire. They are followed by the Black Riders, who are searching for Frodo and the Ring. On their way they meet a new" +
            " friend Aragorn. The Black Riders attacked Frodo and his friends and one of them stabbed Frodo in the shoulder with a cursed knife. Aragorn took Frodo and the" +
            " hobbits to the Rivendell where his wound was healed and the Fellowship of the Ring was formed to take the One Ring to Mordor and destroy it.";

    @Test
    void testUser() {
        UserController userController = new UserController();
        User user = User.builder()
                .id(1)
                .birthdate(birthdate)
                .email(email)
                .login(login)
                .name(name)
                .build();
        assertEquals(user, userController.create(user), USER_NOT_CREATE);
    }

    @Test
    void testWrongBirthdayOfUser() {
        UserController userController = new UserController();
        User user = User.builder()
                .id(1)
                .birthdate(LocalDate.of(2023, 1, 1))
                .email(email)
                .login(login)
                .name(name)
                .build();
        assertNull(userController.create(user), EXCEPTION);
    }

    @Test
    void testEmptyNameOfUser() {
        UserController userController = new UserController();
        User user1 = User.builder()
                .id(1)
                .birthdate(birthdate)
                .email(email)
                .login(login)
                .name(null)
                .build();
        User user2 = User.builder()
                .id(2)
                .birthdate(birthdate)
                .email("123@ru")
                .login(login)
                .name(" ")
                .build();
        assertEquals(login, userController.create(user1).getName(), EXCEPTION);
        assertEquals(login, userController.create(user2).getName(), EXCEPTION);
    }

    @Test
    void testWrongLoginOfUser() {
        UserController userController = new UserController();
        User user1 = User.builder()
                .id(1)
                .birthdate(birthdate)
                .email(email)
                .login(null)
                .name(name)
                .build();
        User user2 = User.builder()
                .id(2)
                .birthdate(birthdate)
                .email(email)
                .login("qwe rty")
                .name(name)
                .build();
        User user3 = User.builder()
                .id(3)
                .birthdate(birthdate)
                .email(email)
                .login("")
                .name(name)
                .build();
        assertNull(userController.create(user1), EXCEPTION);
        assertNull(userController.create(user2), EXCEPTION);
        assertNull(userController.create(user3), EXCEPTION);
    }

    @Test
    void testWrongEmailOfUser() {
        UserController userController = new UserController();
        User user1 = User.builder()
                .id(1)
                .birthdate(birthdate)
                .email("qwerty.ru")
                .login(login)
                .name(name)
                .build();
        User user2 = User.builder()
                .id(2)
                .birthdate(birthdate)
                .email(null)
                .login(login)
                .name(name)
                .build();
        User user3 = User.builder()
                .id(3)
                .birthdate(birthdate)
                .email("")
                .login(login)
                .name(name)
                .build();
        assertNull(userController.create(user1), EXCEPTION);
        assertNull(userController.create(user2), EXCEPTION);
        assertNull(userController.create(user3), EXCEPTION);
    }

    @Test
    void testFilm() {
        FilmController filmController = new FilmController();
        Film film = Film.builder()
                .id(1)
                .name(name)
                .description(description)
                .releaseDate(birthdate)
                .duration(duration)
                .build();
        assertEquals(film, filmController.create(film), FILM_NOT_CREATE);
    }

    @Test
    void testWrongReleaseDateOfFilm() {
        FilmController filmController = new FilmController();
        Film film = Film.builder()
                .id(1)
                .name(name)
                .description(description)
                .releaseDate(LocalDate.of(1696, 1, 1))
                .duration(duration)
                .build();
        assertNull(filmController.create(film), EXCEPTION_FILM);
    }

    @Test
    void testWrongDescriptionOfFilm() {
        FilmController filmController = new FilmController();
        Film film = Film.builder()
                .id(1)
                .name(name)
                .description(bigDescription)
                .releaseDate(birthdate)
                .duration(duration)
                .build();
        assertNull(filmController.create(film), EXCEPTION_FILM);
    }

    @Test
    void testEmptyNameOfFilm() {
        FilmController filmController = new FilmController();
        Film film1 = Film.builder()
                .id(1)
                .name(null)
                .description(description)
                .releaseDate(birthdate)
                .duration(duration)
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("")
                .description(description)
                .releaseDate(birthdate)
                .duration(duration)
                .build();
        assertNull(filmController.create(film1), EXCEPTION_FILM);
        assertNull(filmController.create(film2), EXCEPTION_FILM);
    }
}
