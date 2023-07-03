package ru.yandex.practicum.filmorate.feed.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.feed.mapper.EventMapper;
import ru.yandex.practicum.filmorate.feed.model.Event;
import ru.yandex.practicum.filmorate.feed.model.EventDTO;
import ru.yandex.practicum.filmorate.feed.model.enums.EventType;
import ru.yandex.practicum.filmorate.feed.model.enums.Operation;
import ru.yandex.practicum.filmorate.feed.storage.FeedStorage;
import ru.yandex.practicum.filmorate.user.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FeedServiceImpl implements FeedService {

    private final FeedStorage feedStorage;
    private final UserStorage userStorage;

    @Autowired
    public FeedServiceImpl(FeedStorage feedStorage,UserStorage userStorage) {
        this.feedStorage = feedStorage;
        this.userStorage = userStorage;
    }

    @Override
    public List<EventDTO> getFeed(long id) {
        userStorage.getUserById(id); // для валидации
        return EventMapper.listEventsToListDto(feedStorage.getFeed(id));
    }

    @Override
    public EventDTO saveFeed(long userId, long timestamp, EventType eventType, Operation operation, long entityId) {
        Event eventToSave = Event.builder()
                .userId(userId)
                .timestamp(timestamp)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();
        log.debug("В ленту пользователя с ID {} добавлено событие типа - {} операция - {}.", userId,
                eventType.toString(), operation.toString());
        return EventMapper.eventToDTO(feedStorage.saveFeed(eventToSave));
    }
}
