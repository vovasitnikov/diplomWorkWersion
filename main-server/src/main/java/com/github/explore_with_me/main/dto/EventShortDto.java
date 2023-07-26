package com.github.explore_with_me.main.dto;

import com.github.explore_with_me.main.model.Event;
import com.github.explore_with_me.main.model.RequestState;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@ToString
public class EventShortDto {
    final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String annotation;
    private CategoryDto categoryDto;
    private Integer confirmedRequests;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Integer views;

    public EventShortDto(final Event event) {
        this.annotation = event.getAnotation();
        this.categoryDto = new CategoryDto(event.getCategory());
        this.confirmedRequests = Math.toIntExact(event.getParticipationList().stream().filter(participation -> participation.getStatus().equals(RequestState.CONFIRMED)).count());
        this.eventDate =  CUSTOM_FORMATTER.format(event.getEventDate());
        this.id = event.getId();
        this.initiator = new UserShortDto(event.getUser());
        this.paid = event.isPaid();
        this.title = event.getTitle();
        //TODO: просмотры  - объясните, пожалуйста, как это склеить, как считать просмотры и где их хранить? не понял из ТЗ
        this.views = 999;
    }
}
