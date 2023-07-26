package com.github.explore_with_me.main.controller;

import com.github.explore_with_me.main.dto.EventFullDto;
import com.github.explore_with_me.main.dto.EventShortDto;
import com.github.explore_with_me.main.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Публичный API для работы с событиями
 */
@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EventShortDto>> getAll(@RequestParam(value = "text") String text,
                                                      @RequestParam(value = "categories") Long[] categories,
                                                      @RequestParam(value = "paid") boolean paid,
                                                      @RequestParam(value = "rangeStart") String rangeStart,
                                                      @RequestParam(value = "rangeEnd") String rangeEnd,
                                                      @RequestParam(value = "onlyAvailable") boolean onlyAvailable,
                                                      @RequestParam(value = "sort") String sort,
                                                      @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getAll(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size));
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventFullDto> get(@PathVariable(value = "eventId") Long eventId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEvent(eventId));
    }
}
