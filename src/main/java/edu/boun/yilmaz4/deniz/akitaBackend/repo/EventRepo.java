package edu.boun.yilmaz4.deniz.akitaBackend.repo;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event, Long> {

    Event findEventById(Long id);
    void deleteEventById(Long id);
}
