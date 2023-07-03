package ru.yandex.practicum.filmorate.recommendation;

import ru.yandex.practicum.filmorate.film.model.Film;

import java.util.List;

public interface RecommendationService {

    List<Film> findRecommendation(Long idUser);

}
