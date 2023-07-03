package ru.yandex.practicum.filmorate.mpa.mapper;

import ru.yandex.practicum.filmorate.mpa.model.Mpa;
import ru.yandex.practicum.filmorate.mpa.model.MpaDTO;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MpaMapper {

    private MpaMapper() {
    }

    public static Mpa dtoToMpa(MpaDTO mpaDTO) {
        return Mpa.builder()
                .id(mpaDTO.getId())
                .name(mpaDTO.getName())
                .build();
    }

    public static MpaDTO mpaToDto(Mpa mpa) {
        return MpaDTO.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
    }

    public static List<MpaDTO> listMpaToListDto(Collection<Mpa> mpas) {
        System.out.println(mpas);
        return mpas.stream().map(MpaMapper::mpaToDto).collect(Collectors.toList());
    }

}
