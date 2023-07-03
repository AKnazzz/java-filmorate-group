package ru.yandex.practicum.filmorate.genre.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreDTO {

    private Integer id;
    private String name;
}
