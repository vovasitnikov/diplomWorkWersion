package com.github.explore_with_me.main.repository;

import com.github.explore_with_me.main.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(nativeQuery = true,
            value = "select *\n" +
                    "from events\n" +
                    "where user_id in (:userIds)\n" +
                    "and state in (:states)\n" +
                    "and category_id in (:categories)\n" +
                    "and event_date between :start and :end")
    List<Event> searchEvents(List<Long> userIds, List<String> states, List<Long> categories, LocalDateTime start, LocalDateTime end);
}
