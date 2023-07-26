package com.github.explore_with_me.main.controller;

import com.github.explore_with_me.main.dto.*;
import com.github.explore_with_me.main.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/*
Закрытый API для работы с событиями
 */
@RestController
@RequestMapping("/users")
public class UserEventController {
    private final EventService eventService;

    public UserEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(value = "/{userId}/events", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventFullDto> createEvent(@PathVariable(value = "userId") Long userId,
                                                    @Valid @RequestBody NewEventDto newEventDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.create(userId, newEventDto));
    }

    @RequestMapping(value = "/{userId}/events", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EventShortDto>> getEventsByUser(@PathVariable(value = "userId") Long userId,
                                                               @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                               @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventsByUser(userId));
    }

    @RequestMapping(value = "/{userId}/events/{eventId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventFullDto> updateEventByUser(@PathVariable(value = "userId") Long userId,
                                                          @PathVariable(value = "eventId") Long eventId,
                                                          @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.updateEventByUser(userId, eventId, updateEventUserRequest));
    }

    @RequestMapping(value = "/{userId}/events/{eventId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventFullDto> getOneEventByUser(@PathVariable(value = "userId") Long userId,
                                                          @PathVariable(value = "eventId") Long eventId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getOneEventByUser(userId, eventId));
    }

    @RequestMapping(value = "/{userId}/events/{eventId}/requests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParticipantRequestDto>> getEventRequests(@PathVariable(value = "userId") Long userId,
                                                                        @PathVariable(value = "eventId") Long eventId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventParticipationRequests(userId, eventId));
    }

    @RequestMapping(value = "/{userId}/events/{eventId}/requests", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequestsByUser(@PathVariable(value = "userId") Long userId,
                                                                               @PathVariable(value = "eventId") Long eventId,
                                                                               @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.updateRequestsByUser(userId, eventId, eventRequestStatusUpdateRequest));
    }


}
