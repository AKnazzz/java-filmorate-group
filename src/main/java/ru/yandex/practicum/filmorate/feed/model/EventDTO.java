package ru.yandex.practicum.filmorate.feed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.feed.model.enums.EventType;
import ru.yandex.practicum.filmorate.feed.model.enums.Operation;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    @NotNull
    private long eventId;
    @NotNull
    private long userId;
    private long timestamp;
    private EventType eventType;
    private Operation operation;
    @NotNull
    private long entityId;

}
