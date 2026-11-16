package com.hua.myElga.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hua.myElga.entity.OutboxEvent;
import com.hua.myElga.event.ApplicationNotificationEvent;
import com.hua.myElga.repository.OutboxEventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OutboxService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Transactional
    public void saveEvent(ApplicationNotificationEvent event) {
        try {
            // Parse the event context
            String eventContext = objectMapper.writeValueAsString(event);

            // Create outbox event
            OutboxEvent outboxEvent = new OutboxEvent(event.eventId(), eventContext);

            // Save outbox event in DB
            outboxEventRepository.save(outboxEvent);
        } catch(JsonProcessingException e) {
            throw new IllegalStateException("Failed to convert JSON context in to String", e);
        }
    }
}
