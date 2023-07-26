package com.github.explore_with_me.main.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipantRequestDto> confirmedRequests;
    private List<ParticipantRequestDto> rejectedRequests;
}
