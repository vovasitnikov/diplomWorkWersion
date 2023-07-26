package com.github.explore_with_me.main.dto;

import com.github.explore_with_me.main.model.RequestState;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private RequestState status;
}
