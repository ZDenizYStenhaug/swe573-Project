package edu.boun.yilmaz4.deniz.akitaBackend.repo;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Event;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.RecurringEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepo<T extends Event> extends JpaRepository<T, Long> {

    Event findEventById(Long id);
    void deleteEventById(Long id);

    @Query("Select e from Event e where event_type = 'Event'")
    List<Event> findAllEvents();

    @Query("Select re FROM RecurringEvent re WHERE re.parentEvent = :event")
    List<RecurringEvent> getRecurringEventsOfEvent(@Param("event") Event event);

    @Query("Select re FROM RecurringEvent re WHERE re.parentEvent = :event AND re.date = :date")
    RecurringEvent getRecurringEventByDate(@Param("date")LocalDateTime date, @Param("event") Event event);

}
