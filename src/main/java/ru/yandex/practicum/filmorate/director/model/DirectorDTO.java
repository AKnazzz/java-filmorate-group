package ru.yandex.practicum.filmorate.director.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class DirectorDTO {
    private Long id;

    @NotBlank(message = "Director name can't be blank")
    private String name;
}
