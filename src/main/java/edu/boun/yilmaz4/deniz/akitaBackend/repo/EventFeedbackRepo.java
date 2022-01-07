package edu.boun.yilmaz4.deniz.akitaBackend.repo;

import edu.boun.yilmaz4.deniz.akitaBackend.model.EventFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventFeedbackRepo extends JpaRepository<EventFeedback, Long> {
}
