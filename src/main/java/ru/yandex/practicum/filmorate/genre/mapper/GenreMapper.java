package ru.yandex.practicum.filmorate.genre.mapper;

import ru.yandex.practicum.filmorate.genre.model.Genre;
import ru.yandex.practicum.filmorate.genre.model.GenreDTO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GenreMapper {

    private GenreMapper() {
    }

    public static Genre dtoToGenre(GenreDTO genreDTO) {
        return Genre.builder()
                .id(genreDTO.getId())
                .name(genreDTO.getName())
                .build();
    }

    public static GenreDTO genreToDto(Genre genre) {
        return GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public static List<GenreDTO> listGenreToListDto(Collection<Genre> genres) {
        return genres.stream().map(GenreMapper::genreToDto).collect(Collectors.toList());
    }

}
