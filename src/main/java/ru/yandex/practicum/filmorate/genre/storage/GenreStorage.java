package ru.yandex.practicum.filmorate.genre.storage;

import ru.yandex.practicum.filmorate.genre.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {
    Genre readById(Integer id);

    List<Genre> readAll();

    Set<Genre> getGenresByFilmID(Long filmId);

}
