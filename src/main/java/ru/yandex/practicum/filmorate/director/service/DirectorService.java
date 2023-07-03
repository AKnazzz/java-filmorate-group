package ru.yandex.practicum.filmorate.director.service;

import ru.yandex.practicum.filmorate.director.model.DirectorDTO;

import java.util.List;

public interface DirectorService {
    DirectorDTO saveDirector(DirectorDTO directorDTO);

    DirectorDTO updateDirector(DirectorDTO directorDTO);

    List<DirectorDTO> readAllDirectors();

    DirectorDTO getDirectorById(Long id);

    void deleteDirectorById(Long id);
}
