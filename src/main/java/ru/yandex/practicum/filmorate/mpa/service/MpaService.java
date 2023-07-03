package ru.yandex.practicum.filmorate.mpa.service;

import ru.yandex.practicum.filmorate.mpa.model.MpaDTO;

import java.util.List;

public interface MpaService {

    MpaDTO readById(Integer id);

    List<MpaDTO> readAll();

}
