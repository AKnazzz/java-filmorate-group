package ru.yandex.practicum.filmorate.mpa.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.mpa.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.mpa.model.MpaDTO;
import ru.yandex.practicum.filmorate.mpa.storage.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaServiceImp implements MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaServiceImp(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public MpaDTO readById(Integer id) {
        return MpaMapper.mpaToDto(mpaStorage.readById(id));
    }

    @Override
    public List<MpaDTO> readAll() {
        return MpaMapper.listMpaToListDto(mpaStorage.readAll());
    }

}
