package com.github.explore_with_me.main.dto;

import com.github.explore_with_me.main.model.Participation;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Data
@ToString
@NoArgsConstructor
public class ParticipantRequestDto {
    final static DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String created;
    private Long event;
    private Long id;
    private Long requester;
    private String status;

    public ParticipantRequestDto(final Participation participation) {
        this.created = CUSTOM_FORMATTER.format(participation.getCreated());
        this.event = participation.getEvent().getId();
        this.id = participation.getId();
        this.requester = participation.getUser().getId();
        this.status = participation.getStatus().name();
    }
}
