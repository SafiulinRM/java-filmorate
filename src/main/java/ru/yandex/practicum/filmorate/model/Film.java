package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film implements Comparable<Film> {
    private int id = 0;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private final Set<Integer> likes = new HashSet<>();

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void removeLike(int userId) {
        likes.remove(userId);
    }

    @Override
    public int compareTo(Film o) {
        if (o.likes.size() != this.likes.size()) {
            return o.likes.size() - this.likes.size();
        } else {
            return 1;
        }
    }
}
