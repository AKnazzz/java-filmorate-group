package ru.yandex.practicum.filmorate.director.storage;

import ru.yandex.practicum.filmorate.director.model.Director;

import java.util.List;
import java.util.Set;

public interface DirectorStorage {
    Director saveDirector(Director director);

    Director updateDirector(Director director);

    List<Director> readAllDirectors();

    Director getDirectorById(Long id);

    void deleteDirectorById(Long id);

    Set<Director> getDirectorsByFilmId(Long filmId);
}
