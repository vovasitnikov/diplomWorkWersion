package com.github.explore_with_me.main.repository;

import com.github.explore_with_me.main.model.Event;
import com.github.explore_with_me.main.model.Participation;
import com.github.explore_with_me.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    List<Participation> getAllByUserAndAndEvent(final User user, final Event event);

    @Query(nativeQuery = true,
            value = "select count(*)\n" +
                    "from events e join participations p  on e.id = p.event_id\n" +
                    "where e.id = :eventId\n" +
                    "and e.user_id = :userId\n" +
                    "and p.participation_status = 'CONFIRMED'")
    Integer getAmountOfParticipantsForEvent(@Param(value = "userId") final Long userId,
                                            @Param(value = "eventId") final Long eventId);
}
