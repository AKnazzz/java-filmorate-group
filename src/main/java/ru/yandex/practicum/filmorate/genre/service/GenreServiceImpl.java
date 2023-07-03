package ru.yandex.practicum.filmorate.genre.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.genre.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.genre.model.GenreDTO;
import ru.yandex.practicum.filmorate.genre.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreServiceImpl(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public GenreDTO readById(Integer id) {
        return GenreMapper.genreToDto(genreStorage.readById(id));
    }

    @Override
    public List<GenreDTO> readAll() {
        return GenreMapper.listGenreToListDto(genreStorage.readAll());
    }

}
