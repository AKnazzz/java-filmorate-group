package ru.yandex.practicum.filmorate.genre.service;

import ru.yandex.practicum.filmorate.genre.model.GenreDTO;

import java.util.List;

public interface GenreService {

    GenreDTO readById(Integer id);

    List<GenreDTO> readAll();

}
