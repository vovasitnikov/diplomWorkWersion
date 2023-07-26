package com.github.explore_with_me.main.dto;

import com.github.explore_with_me.main.model.Event;
import com.github.explore_with_me.main.model.Location;
import com.github.explore_with_me.main.model.RequestState;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@ToString
public class EventFullDto {
    final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String annotation;
    private CategoryDto categoryDto;
    private Integer confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Location location;
    private boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private boolean requestModeration;
    private String state;
    private String title;
    private Integer views;

    public EventFullDto(final Event event) {
        this.annotation = event.getAnotation();
        this.categoryDto = new CategoryDto(event.getCategory());
        this.confirmedRequests = Math.toIntExact(event.getParticipationList().stream().filter(participation -> participation.getStatus().equals(RequestState.CONFIRMED)).count());
        this.createdOn = CUSTOM_FORMATTER.format(event.getCreatedOn());
        this.description = event.getDescription();
        this.eventDate = CUSTOM_FORMATTER.format(event.getEventDate());
        this.id = event.getId();
        this.initiator = new UserShortDto(event.getUser());
        this.location = event.getLocation();
        this.paid = event.isPaid();
        this.participantLimit = event.getParticipantLimit();
        this.publishedOn = event.getPublishedOn() != null ? CUSTOM_FORMATTER.format(event.getPublishedOn()) : "";
        this.requestModeration = event.isRequestModeration();
        this.state = event.getState().name();
        this.title = event.getTitle();
        //TODO: просмотры  - объясните, пожалуйста, как это склеить, как считать просмотры и где их хранить? не понял из ТЗ
        this.views = 999;
    }
}
