package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {
    private final LocalDate birthday = LocalDate.of(1996, 1, 1);
    private final String email = "qwerty@gmail.com";
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

    @Test
    void testUser() throws ValidationException {
        UserController userController = new UserController();
        User user = User.builder()
                .id(1)
                .birthday(birthday)
                .email(email)
                .login(login)
                .name(name)
                .build();
        assertEquals(user, userController.create(user), "Пользователь не создался");
    }

    @Test
    void testWrongBirthdayOfUser() {
        UserController userController = new UserController();
        User user = User.builder()
                .id(1)
                .birthday(LocalDate.of(2023, 1, 1))
                .email(email)
                .login(login)
                .name(name)
                .build();
        assertThrows(ValidationException.class, () -> userController.create(user), "Тест на фильм не пройден");
    }

    @Test
    void testEmptyNameOfUser() throws ValidationException {
        UserController userController = new UserController();
        User user1 = User.builder()
                .id(1)
                .birthday(birthday)
                .email(email)
                .login(login)
                .name(null)
                .build();
        User user2 = User.builder()
                .id(2)
                .birthday(birthday)
                .email("123@ru")
                .login(login)
                .name(" ")
                .build();
        assertEquals(login, userController.create(user1).getName(), "Тест на пользователя не пройден");
        assertEquals(login, userController.create(user2).getName(), "Тест на пользователя не пройден");
    }

    @Test
    void testWrongLoginOfUser() {
        UserController userController = new UserController();
        User user1 = User.builder()
                .id(1)
                .birthday(birthday)
                .email(email)
                .login(null)
                .name(name)
                .build();
        User user2 = User.builder()
                .id(2)
                .birthday(birthday)
                .email(email)
                .login("qwe rty")
                .name(name)
                .build();
        User user3 = User.builder()
                .id(3)
                .birthday(birthday)
                .email(email)
                .login("")
                .name(name)
                .build();
        assertThrows(ValidationException.class, () -> userController.create(user1), "Тест на фильм не пройден");
        assertThrows(ValidationException.class, () -> userController.create(user2), "Тест на фильм не пройден");
        assertThrows(ValidationException.class, () -> userController.create(user3), "Тест на фильм не пройден");
    }

    @Test
    void testWrongEmailOfUser() {
        UserController userController = new UserController();
        User user1 = User.builder()
                .id(1)
                .birthday(birthday)
                .email("qwerty.ru")
                .login(login)
                .name(name)
                .build();
        User user2 = User.builder()
                .id(2)
                .birthday(birthday)
                .email(null)
                .login(login)
                .name(name)
                .build();
        User user3 = User.builder()
                .id(3)
                .birthday(birthday)
                .email("")
                .login(login)
                .name(name)
                .build();
        assertThrows(ValidationException.class, () -> userController.create(user1), "Тест на фильм не пройден");
        assertThrows(ValidationException.class, () -> userController.create(user2), "Тест на фильм не пройден");
        assertThrows(ValidationException.class, () -> userController.create(user3), "Тест на фильм не пройден");
    }

    @Test
    void testFilm() throws ValidationException {
        FilmController filmController = new FilmController();
        Film film = Film.builder()
                .id(1)
                .name(name)
                .description(description)
                .releaseDate(birthday)
                .duration(duration)
                .build();
        assertEquals(film, filmController.create(film), "Фильм не внесен");
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
        assertThrows(ValidationException.class, () -> filmController.create(film), "Тест на фильм не пройден");
    }

    @Test
    void testWrongDescriptionOfFilm() {
        FilmController filmController = new FilmController();
        Film film = Film.builder()
                .id(1)
                .name(name)
                .description(bigDescription)
                .releaseDate(birthday)
                .duration(duration)
                .build();
        assertThrows(ValidationException.class, () -> filmController.create(film), "Тест на фильм не пройден");
    }

    @Test
    void testEmptyNameOfFilm() {
        FilmController filmController = new FilmController();
        Film film1 = Film.builder()
                .id(1)
                .name(null)
                .description(description)
                .releaseDate(birthday)
                .duration(duration)
                .build();
        Film film2 = Film.builder()
                .id(2)
                .name("")
                .description(description)
                .releaseDate(birthday)
                .duration(duration)
                .build();
        assertThrows(ValidationException.class, () -> filmController.create(film1), "Тест на фильм не пройден");
        assertThrows(ValidationException.class, () -> filmController.create(film2), "Тест на фильм не пройден");
    }
}
