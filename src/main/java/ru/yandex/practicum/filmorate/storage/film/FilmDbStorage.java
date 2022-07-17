package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film getById(long id) {
        final String sqlQuery = "select * from FILMS where FILM_ID = ?";
        final List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, id);
        if (films.size() != 1) {
            throw new NotFoundException("Фильма с таким id отсутствует, текущий: " + id);
        }
        films.get(0).setGenres(getGenresByIdUser(id));
        films.get(0).getMpa().setName(getNameMpaById(films.get(0).getMpa().getId()));
        return films.get(0);
    }

    static String getNameMpa(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("NAME");
    }

    private String getNameMpaById(int id) {
        final String sqlQueryMpa = "select NAME from MPA_RATINGS where MPA_RATING_ID = ?";
        final List<String> mpa = jdbcTemplate.query(sqlQueryMpa, FilmDbStorage::getNameMpa, id);
        return mpa.get(0);
    }

    static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getLong("FILM_ID"),
                rs.getString("NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                new MpaRating(rs.getInt("MPA_RATING_ID"), null),
                null
        );
    }

    private List<Genre> getGenresByIdUser(long id) {
        final String sqlQuery = "select * from GENRES where GENRE_ID IN (select GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?)";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, GenreDbStorage::makeGenre, id);
        return genres;
    }

    @Override
    public List<Film> getAll() {
        final String sqlQuery = "select * from FILMS";
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm);
        for (Film film : films) {
            film.setGenres(getGenresByIdUser(film.getId()));
            film.getMpa().setName(getNameMpaById(film.getMpa().getId()));
        }
        return films;
    }

    @Override
    public Film save(Film film) {
        String sqlQuery = "insert into FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID) values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                String sqlQueryGenre = "insert into FILM_GENRES (FILM_ID, GENRE_ID) values (?, ?)";
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlQueryGenre);
                    stmt.setLong(1, film.getId());
                    stmt.setInt(2, genre.getId());
                    return stmt;
                });
            }
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "merge into FILMS (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_RATING_ID) values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setLong(1, film.getId());
            stmt.setString(2, film.getName());
            stmt.setString(3, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(releaseDate));
            }
            stmt.setInt(5, film.getDuration());
            stmt.setInt(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        String sqlQueryDeleteGenres = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQueryDeleteGenres, film.getId());
        if (film.getGenres() != null) {
            Set<Integer> ids = new HashSet<>();
            for (Genre genre : film.getGenres()) {
                if (!ids.contains(genre.getId())) {
                    String sqlQueryGenre = "insert into FILM_GENRES (FILM_ID, GENRE_ID) values (?, ?)";
                    jdbcTemplate.update(connection -> {
                        PreparedStatement stmt = connection.prepareStatement(sqlQueryGenre);
                        stmt.setLong(1, film.getId());
                        stmt.setInt(2, genre.getId());
                        return stmt;
                    });
                    ids.add(genre.getId());
                }
            }
        }
        return getById(film.getId());
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sqlQuery = "insert into LIKES (FILM_ID, LIKE_ID) values (?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery);
            stmt.setLong(1, filmId);
            stmt.setLong(2, userId);
            return stmt;
        });
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String sqlQueryGenre = "DELETE FROM LIKES WHERE FILM_ID = ? AND LIKE_ID = ?";
        jdbcTemplate.update(sqlQueryGenre, filmId, userId);
    }

    @Override
    public List<Film> getListOfBestFilms(int count) {
        final String sqlQuery = "select F.* from FILMS AS F LEFT JOIN LIKES AS L ON F.FILM_ID = L.FILM_ID" +
                " GROUP BY F.FILM_ID ORDER BY COUNT(LIKE_ID) DESC LIMIT ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::makeFilm, count);
        for (Film film : films) {
            film.setGenres(getGenresByIdUser(film.getId()));
            film.getMpa().setName(getNameMpaById(film.getMpa().getId()));
        }
        return films;
    }
}
