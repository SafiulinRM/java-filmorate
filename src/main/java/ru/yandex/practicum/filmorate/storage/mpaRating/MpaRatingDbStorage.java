package ru.yandex.practicum.filmorate.storage.mpaRating;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaRatingDbStorage implements MpaRatingStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating getById(int id) {
        final String sqlQuery = "select * from MPA_RATINGS where MPA_RATING_ID = ?";
        final List<MpaRating> mpaRatings = jdbcTemplate.query(sqlQuery, MpaRatingDbStorage::makeMpaRating, id);
        if (mpaRatings.size() != 1) {
            throw new NotFoundException("Рейтинга с таким id отсутствует, текущий: " + id);
        }
        return mpaRatings.get(0);
    }

    static MpaRating makeMpaRating(ResultSet rs, int rowNum) throws SQLException {
        return new MpaRating(rs.getInt("MPA_RATING_ID"),
                rs.getString("NAME"));
    }

    @Override
    public List<MpaRating> getAll() {
        final String sqlQuery = "select * from MPA_RATINGS";
        return jdbcTemplate.query(sqlQuery, MpaRatingDbStorage::makeMpaRating);
    }
}
