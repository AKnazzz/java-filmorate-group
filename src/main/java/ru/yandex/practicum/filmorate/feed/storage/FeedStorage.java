package ru.yandex.practicum.filmorate.feed.storage;

import ru.yandex.practicum.filmorate.feed.model.Event;

import java.util.List;

public interface FeedStorage {

    List<Event> getFeed(long id);

    Event saveFeed(Event event);
}
