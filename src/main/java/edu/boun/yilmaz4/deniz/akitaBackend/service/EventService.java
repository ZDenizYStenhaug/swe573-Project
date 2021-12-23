package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Event;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.EventStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.EventRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);

    @Autowired
    private EventRepo eventRepo;

    @Transactional
    public Event addEvent(Event event) {
        event.setStatus(EventStatus.getDefault());
        logger.info("Saving event " + event);
        return eventRepo.save(event);
    }

}
