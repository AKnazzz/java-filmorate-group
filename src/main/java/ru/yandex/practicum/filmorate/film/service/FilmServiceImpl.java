package ru.yandex.practicum.filmorate.film.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.feed.model.enums.EventType;
import ru.yandex.practicum.filmorate.feed.model.enums.Operation;
import ru.yandex.practicum.filmorate.feed.service.FeedService;
import ru.yandex.practicum.filmorate.film.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.film.model.Film;
import ru.yandex.practicum.filmorate.film.model.FilmDTO;
import ru.yandex.practicum.filmorate.film.storage.FilmStorage;
import ru.yandex.practicum.filmorate.like.service.LikeService;
import ru.yandex.practicum.filmorate.user.service.UserService;
import ru.yandex.practicum.filmorate.validation.Validator;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final LikeService likeService;
    private final FeedService feedService;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserService userService,
            LikeService likeService, FeedService feedService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likeService = likeService;
        this.feedService = feedService;
    }

    @Override
    public FilmDTO saveFilm(FilmDTO filmDTO) {
        Film film = FilmMapper.dtoToFilm(filmDTO);
        if (Validator.filmValidator(film)) {
            log.debug("Film {} сохранён.", filmDTO.getId());
            return FilmMapper.filmToDTO(filmStorage.saveFilm(film));
        }
        throw new ValidationException("Валидация Film " + filmDTO + " не пройдена");
    }

    @Override
    public FilmDTO updateFilm(FilmDTO filmDTO) {
        Film film = FilmMapper.dtoToFilm(filmDTO);
        if (Validator.filmValidator(film)) {
            log.debug("Film c ID {} обновлён.", filmDTO.getId());
            return FilmMapper.filmToDTO(filmStorage.updateFilm(film));
        }
        throw new ValidationException("Валидация Film " + filmDTO + " не пройдена");
    }

    @Override
    public List<FilmDTO> readAllFilms() {
        log.debug("Полный список Films возвращён.");
        return FilmMapper.listFilmsToListDto(filmStorage.readAllFilms());
    }

    @Override
    public FilmDTO getFilmByID(Long id) {
        log.debug("Film c ID {} возвращён.", id);
        return FilmMapper.filmToDTO(filmStorage.getFilmById(id));
    }

    @Override
    public void deleteLikeById(Long idFilm, Long idUser) {
        filmStorage.getFilmById(idFilm);
        userService.getUserById(idUser); // для валидации
        likeService.deleteLike(idFilm, idUser);
        feedService.saveFeed(idUser, Instant.now().toEpochMilli(), EventType.LIKE, Operation.REMOVE, idFilm);
        log.debug("Лайк User c ID {} удалён у Film c ID {}", idUser, idFilm);
    }

    @Override
    public void userLike(Long idFilm, Long idUser) {
        filmStorage.getFilmById(idFilm);
        userService.getUserById(idUser); // для валидации
        likeService.addLike(idFilm, idUser);
        feedService.saveFeed(idUser, Instant.now().toEpochMilli(), EventType.LIKE, Operation.ADD, idFilm);
        log.debug("Лайк у User c ID {} установлен Film c ID {}.", idUser, idFilm);
    }

    @Override
    public List<FilmDTO> getTopFilms(Long count, Long genreId, Long year) {
        log.debug("Получен список из {} Film по кол-ву Likes.", count);
        return FilmMapper.listFilmsToListDto(filmStorage.getTopFilms(count, genreId, year));
    }


    @Override
    public List<FilmDTO> getCommonFilms(Long userId, Long friendId) {
        return FilmMapper.listFilmsToListDto(filmStorage.getCommonFilms(userId, friendId));
    }

    @Override
    public void deleteFilm(Long id) {
        filmStorage.deleteFilm(id);
    }

    @Override
    public List<FilmDTO> getSortedFilms(Long id, String sortBy) {
        List<FilmDTO> sortedFilms = FilmMapper.listFilmsToListDto(filmStorage.getSortedFilms(id, sortBy));
        log.debug("Получен список фильмов режиссера с ID {}, отсортированный по кол-ву лайков или годам", id);
        if (sortedFilms.isEmpty()) {
            throw new EntityNotFoundException("Режиссер с id: " + id + " не найден");
        }
        return sortedFilms;
    }

    public List<FilmDTO> searchFilm(String query, String by) {
        return FilmMapper.listFilmsToListDto(search(query, by));
    }

    public List<Film> search(String query, String by) {
        if (query == null && by == null) {
            return new ArrayList<>(filmStorage.getTopFilms(null, null, null));
        }
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }
        if (by.contains("director") && by.contains("title")) {
            return filmStorage.searchFilmForTitleAndDirector(query);
        } else if (by.contains("director")) {
            return filmStorage.searchFilmForDirector(query);
        } else if (by.contains("title")) {
            return filmStorage.searchFilmForTitle(query);
        }
        return null;
    }
}
