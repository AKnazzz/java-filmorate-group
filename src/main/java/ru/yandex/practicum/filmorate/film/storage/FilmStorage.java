package ru.yandex.practicum.filmorate.film.storage;

import ru.yandex.practicum.filmorate.film.model.Film;

import java.util.List;

public interface FilmStorage {

    Film saveFilm(Film film);

    Film updateFilm(Film film);

    List<Film> readAllFilms();

    Film getFilmById(Long id);

    List<Film> getTopFilms(Long count, Long genreId, Long year);

    List<Film> getCommonFilms(Long userId, Long friendId);

    void deleteFilm(Long id);

    List<Film> getSortedFilms(Long id, String sortBy);

    List<Film> searchFilmForDirector(String queryStr);

    List<Film> searchFilmForTitle(String queryStr);

    List<Film> searchFilmForTitleAndDirector(String queryStr);
}
