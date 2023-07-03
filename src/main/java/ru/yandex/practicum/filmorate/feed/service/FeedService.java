package ru.yandex.practicum.filmorate.feed.service;

import ru.yandex.practicum.filmorate.feed.model.EventDTO;
import ru.yandex.practicum.filmorate.feed.model.enums.EventType;
import ru.yandex.practicum.filmorate.feed.model.enums.Operation;

import java.util.List;

public interface FeedService {

    List<EventDTO> getFeed(long id);

    EventDTO saveFeed(long userId, long timestamp, EventType eventType, Operation operation, long entityId);
}
