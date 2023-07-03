package ru.yandex.practicum.filmorate.feed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.feed.model.enums.EventType;
import ru.yandex.practicum.filmorate.feed.model.enums.Operation;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private long eventId;
    private long userId;
    private long timestamp;
    private EventType eventType;
    private Operation operation;
    private long entityId;

}
