package com.github.explore_with_me.main.service;

import com.github.explore_with_me.main.dto.*;
import com.github.explore_with_me.main.exception.DateValidationException;
import com.github.explore_with_me.main.exception.EventRequestValidationException;
import com.github.explore_with_me.main.exception.NotFoundException;
import com.github.explore_with_me.main.model.*;
import com.github.explore_with_me.main.repository.CategoryRepository;
import com.github.explore_with_me.main.repository.EventRepository;
import com.github.explore_with_me.main.repository.ParticipationRepository;
import com.github.explore_with_me.main.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRepository participationRepository;
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventService(EventRepository eventRepository, UserRepository userRepository, CategoryRepository categoryRepository, ParticipationRepository participationRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.participationRepository = participationRepository;
    }

    public EventFullDto create(final Long userId, final NewEventDto newEventDto) {
        validate(newEventDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with such userId=" + userId + " not found"));
        Category category = categoryRepository.findById(newEventDto.getCategoryId()).orElseThrow(() -> new NotFoundException("Category with such categoryId=" + newEventDto.getCategoryId() + " not found"));
        Event event = new Event();
        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER));
        event.setPaid(newEventDto.isPaid());
        event.setLocation(newEventDto.getLocation());
        event.setCategory(category);
        event.setAnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setTitle(newEventDto.getTitle());
        event.setUser(user);
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.isRequestModeration());
        eventRepository.save(event);
        return new EventFullDto(event);
    }

    public List<EventShortDto> getEventsByUser(final Long userId) {
        User user = checkUserExists(userId);
        List<EventShortDto> result = new ArrayList<>();
        if (user.getEvents().size() > 0) {
            user.getEvents().forEach(e -> result.add(new EventShortDto(e)));
        }
        return result;
    }

    public EventFullDto getOneEventByUser(final Long userId, final Long eventId) {
        checkObjectExists(userId);
        Event event = checkEventExists(eventId);
        return new EventFullDto(event);
    }

    public EventFullDto updateEventByAdmin(final Long eventId, final UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = checkEventExists(eventId);
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("Cannot publish the event because it's not in the right state: " + event.getState());
        }
        Category category;
        if (updateEventAdminRequest.getCategoryId() != null) {
            category = categoryRepository.findById(updateEventAdminRequest.getCategoryId()).orElseThrow(() -> new NotFoundException("Category with such categoryId=" + updateEventAdminRequest.getCategoryId() + " not found"));
        } else {
            category = event.getCategory();
        }
        event.setCategory(category);
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            if (LocalDateTime.parse(updateEventAdminRequest.getEventDate(), FORMATTER).isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DateValidationException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + updateEventAdminRequest.getEventDate());
            }
            event.setEventDate(LocalDateTime.parse(updateEventAdminRequest.getEventDate(), FORMATTER));
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocation(updateEventAdminRequest.getLocation());
        }
        event.setPaid(updateEventAdminRequest.isPaid());
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        event.setRequestModeration(updateEventAdminRequest.isRequestModeration());
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(EventState.PUBLISH_EVENT) || updateEventAdminRequest.getStateAction().equals(EventState.REJECT_EVENT)) {
                if (updateEventAdminRequest.getStateAction().equals(EventState.PUBLISH_EVENT)) {
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                } else {
                    event.setState(EventState.CANCELED);
                }
            } else {
                throw new ValidationException("State should be in PUBLISH_EVENT, REJECT_EVENT");
            }
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        eventRepository.save(event);
        return new EventFullDto(event);
    }

    public EventFullDto updateEventByUser(final Long userId, final Long eventId, final UpdateEventUserRequest updateEventUserRequest) {
        checkUserExists(userId);
        Event event = checkEventExists(eventId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Event must not be published");
        }
        Category category;
        if (updateEventUserRequest.getCategoryId() != null) {
            category = categoryRepository.findById(updateEventUserRequest.getCategoryId()).orElseThrow(() -> new NotFoundException("Category with such categoryId=" + updateEventUserRequest.getCategoryId() + " not found"));
        } else {
            category = event.getCategory();
        }
        event.setCategory(category);
        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getEventDate() != null) {
            if (LocalDateTime.parse(updateEventUserRequest.getEventDate(), FORMATTER).isBefore(LocalDateTime.now().plusHours(2))) {
                throw new DateValidationException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + updateEventUserRequest.getEventDate());
            }
            event.setEventDate(LocalDateTime.parse(updateEventUserRequest.getEventDate(), FORMATTER));
        }
        if (updateEventUserRequest.getLocation() != null) {
            event.setLocation(updateEventUserRequest.getLocation());
        }
        event.setPaid(updateEventUserRequest.isPaid());
        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        event.setRequestModeration(updateEventUserRequest.isRequestModeration());
        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction().equals(EventState.CANCEL_REVIEW) || updateEventUserRequest.getStateAction().equals(EventState.SEND_TO_REVIEW)) {
                event.setState(updateEventUserRequest.getStateAction());
            } else {
                throw new ValidationException("State should be in CANCEL_REVIEW, SEND_TO_REVIEW");
            }
        }
        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }
        eventRepository.save(event);
        return new EventFullDto(event);
    }

    public List<ParticipantRequestDto> getEventParticipationRequests(final Long userId, final Long eventId) {
        User user = checkUserExists(userId);
        Event event = checkEventExists(eventId);
        List<Participation> participationList = participationRepository.getAllByUserAndAndEvent(user, event);
        List<ParticipantRequestDto> result = new ArrayList<>();
        if (participationList.size() > 0) {
            participationList.forEach(p -> result.add(new ParticipantRequestDto(p)));
        }
        return result;
    }

    public EventRequestStatusUpdateResult updateRequestsByUser(final Long userId, final Long eventId, final EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        Event event = checkEventExists(eventId);
        checkObjectExists(userId, eventId);
        AtomicReference<Integer> existingParticipantsAmount = new AtomicReference<>(participationRepository.getAmountOfParticipantsForEvent(userId, eventId));
        List<Participation> currentRequests = new ArrayList<>();
        List<ParticipantRequestDto> rejectedRequests = new ArrayList<>();
        List<ParticipantRequestDto> confirmedRequests = new ArrayList<>();
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        //если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
        if (!event.getParticipantLimit().equals(0) || event.isRequestModeration()) {
            if (eventRequestStatusUpdateRequest.getRequestIds().size() > 0) {
                eventRequestStatusUpdateRequest.getRequestIds().forEach(r -> currentRequests.add(participationRepository.findById(r.longValue()).get()));
                //нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
                if (Objects.equals(event.getParticipantLimit(), existingParticipantsAmount.get())) {
                    throw new EventRequestValidationException("The participant limit has been reached");
                } else {
                    for (Participation participation : currentRequests) {
                        //статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
                        if (!participation.getStatus().equals(RequestState.PENDING)) {
                            throw new EventRequestValidationException("Request must have status PENDING");
                        } else {
                            participation.setStatus(RequestState.CONFIRMED);
                            participationRepository.save(participation);
                            confirmedRequests.add(new ParticipantRequestDto(participation));
                            currentRequests.remove(participation);
                            existingParticipantsAmount.updateAndGet(v -> v + 1);
                            //если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
                            if (existingParticipantsAmount.get().equals(event.getParticipantLimit())) {
                                break;
                            }
                        }
                    }
                    if (currentRequests.size() > 0) {
                        for (Participation p : currentRequests) {
                            p.setStatus(RequestState.REJECTED);
                            rejectedRequests.add(new ParticipantRequestDto(p));
                        }
                        participationRepository.saveAll(currentRequests);
                    }
                }

            }
        }
        result.setRejectedRequests(rejectedRequests);
        result.setConfirmedRequests(confirmedRequests);
        return result;
    }

    public List<EventFullDto> searchEvents(Long[] users, String[] states, Long[] categories,
                                           String rangeStart, String rangeEnd, Integer from, Integer size) {
        List<EventFullDto> result = new ArrayList<>();
        List<Event> foundEvents = eventRepository.searchEvents(Arrays.asList(users), Arrays.asList(states), Arrays.asList(categories),
                LocalDateTime.parse(rangeStart, FORMATTER),
                LocalDateTime.parse(rangeEnd, FORMATTER));
        if (foundEvents.size() > 0) {
            foundEvents.forEach(e -> result.add(new EventFullDto(e)));
        }
        return result;
    }

    //TODO: доделать
    public List<EventShortDto> getAll(String text, Long[] categories, boolean paid,
                                      String rangeStart, String rangeEnd, boolean onlyAvailable,
                                      String sort, Integer from, Integer size) {
        return Collections.emptyList();
    }

    public EventFullDto getEvent(final Long eventId) {
        Event event = checkEventExists(eventId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            //TODO: информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
            return new EventFullDto(event);
        } else return null;
    }

    private User checkUserExists(final Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with such userId=" + userId + " not found"));
    }

    private Event checkEventExists(final Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with such eventId=" + eventId + " not found"));
    }

    private void checkObjectExists(final Long userId, final Long eventId) {
        checkObjectExists(userId);
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event with such eventId=" + eventId + " not found"));
    }

    private void checkObjectExists(final Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with such userId=" + userId + " not found"));
    }

    private void validate(NewEventDto newEventDto) {
        if (newEventDto.getAnnotation() == null)
            throw new ValidationException("Event annotation cannot be empty.");
        if (newEventDto.getAnnotation().isBlank())
            throw new ValidationException("Event annotation cannot be empty.");
        if (newEventDto.getCategoryId() == null)
            throw new ValidationException("Event category cannot be empty.");
        if (newEventDto.getDescription() == null)
            throw new ValidationException("Event description cannot be empty.");
        if (newEventDto.getDescription().isBlank())
            throw new ValidationException("Event description cannot be empty.");
        if (newEventDto.getEventDate() == null)
            throw new ValidationException("Event date cannot be empty.");
        if (newEventDto.getEventDate().isBlank())
            throw new ValidationException("Event date cannot be empty.");
        //дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента (Ожидается код ошибки 409)
        if (LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER).isBefore(LocalDateTime.now().plusHours(2)))
            throw new DateValidationException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + newEventDto.getEventDate());
        if (newEventDto.getLocation() == null)
            throw new ValidationException("Event location cannot be empty.");
        if (newEventDto.getTitle() == null)
            throw new ValidationException("Event title cannot be empty.");
        if (newEventDto.getTitle().isBlank())
            throw new ValidationException("Event title cannot be empty.");
    }
}
