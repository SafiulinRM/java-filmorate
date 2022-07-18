package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getById(int id) {
        final String sqlQuery = "select * from GENRES where GENRE_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), id);
        if (genres.size() != 1) {
            throw new NotFoundException("Жанр с таким id отсутствует, текущий: " + id);
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getAll() {
        final String sqlQuery = "select * from GENRES ORDER BY GENRE_ID";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("NAME"));
    }
}
