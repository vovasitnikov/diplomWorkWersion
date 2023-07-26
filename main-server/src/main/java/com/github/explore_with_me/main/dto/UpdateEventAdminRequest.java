package com.github.explore_with_me.main.dto;

import com.github.explore_with_me.main.model.Event;
import com.github.explore_with_me.main.model.EventState;
import com.github.explore_with_me.main.model.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Data
@ToString
@NoArgsConstructor
public class UpdateEventAdminRequest {
    final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String annotation;
    private Long categoryId;
    private String description;
    private String eventDate;
    private Location location;
    private boolean paid;
    private Integer participantLimit;
    private boolean requestModeration;
    private EventState stateAction;
    private String title;

    public UpdateEventAdminRequest(final Event event) {
        this.annotation = event.getAnotation();
        this.eventDate = event.getEventDate().format(CUSTOM_FORMATTER);
        this.paid = event.isPaid();
        this.description = event.getDescription();
        this.categoryId = event.getCategory().getId();
        this.location = event.getLocation();
        this.title = event.getTitle();
        this.participantLimit = event.getParticipantLimit();
        this.requestModeration = event.isRequestModeration();
    }
}
