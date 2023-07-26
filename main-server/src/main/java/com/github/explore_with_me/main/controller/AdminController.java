package com.github.explore_with_me.main.controller;

import com.github.explore_with_me.main.dto.*;
import com.github.explore_with_me.main.service.CategoryService;
import com.github.explore_with_me.main.service.EventService;
import com.github.explore_with_me.main.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final CategoryService categoryService;
    private final UserService userService;
    private final EventService eventService;

    public AdminController(CategoryService categoryService, UserService userService, EventService eventService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.eventService = eventService;
    }

    //Categories
    @RequestMapping(value = "/categories", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> createCategory(@RequestBody NewCategoryDto newCategoryDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.create(newCategoryDto));
    }

    @RequestMapping(value = "/categories/{catId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable(value = "catId") Long catId,
                                                      @RequestBody NewCategoryDto newCategoryDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryService.update(catId, newCategoryDto));
    }

    @RequestMapping(value = "/categories/{catId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteCategory(@PathVariable(value = "catId") Long catId) {
        categoryService.delete(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Users
    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.create(newUserRequest));
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(value = "ids") Long[] ids,
                                                     @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                     @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUsers(ids, from, size));
    }

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId") Long userId) {
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //Events
    @RequestMapping(value = "/events", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EventFullDto>> searchEvents(@RequestParam(value = "users", required = false) Long[] users,
                                                           @RequestParam(value = "states", required = false) String[] states,
                                                           @RequestParam(value = "categories", required = false) Long[] categories,
                                                           @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                                           @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                                           @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
                                                           @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.searchEvents(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @RequestMapping(value = "/events/{eventId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable(value = "eventId") Long eventId,
                                                    @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.updateEventByAdmin(eventId, updateEventAdminRequest));
    }

    //Compilations


}
