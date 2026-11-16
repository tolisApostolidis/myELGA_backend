package com.hua.myElga.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hua.myElga.entity.OutboxEvent;
import com.hua.myElga.event.ApplicationNotificationEvent;
import com.hua.myElga.event.OutboxEventStatus;
import com.hua.myElga.repository.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class OutboxPublisher {

    private static final Logger logger = LoggerFactory.getLogger(OutboxPublisher.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationNotificationPublisher applicationNotificationPublisher;

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {
        // Retrieve 10 oldest pending events from DB
        List<OutboxEvent> pendingEvents = outboxEventRepository.findTop10ByStatusOrderByDateOfCreationAsc(OutboxEventStatus.PENDING);

        for (OutboxEvent outboxEvent : pendingEvents) {
            processEvent(outboxEvent);
        }
    }

    private void processEvent(OutboxEvent outboxEvent) {
        try {
            // Parse the event
            ApplicationNotificationEvent event = objectMapper.readValue(outboxEvent.getEvent(), ApplicationNotificationEvent.class);

            // Try to publish the event
            boolean published = applicationNotificationPublisher.publishEvent(event);

            if (published) {
                // Set state and publication date
                outboxEvent.setStatus(OutboxEventStatus.PUBLISHED);
                outboxEvent.setDateOfPublication(Instant.now());

                // Store it in DB
                outboxEventRepository.save(outboxEvent);

                logger.info("Successfully published outbox event: " + outboxEvent.getEventId());
            } else {
                logger.warn("Outbox event remains PENDING, event id: {}", outboxEvent.getEventId());
            }
        } catch (Exception e) {
            logger.error("Failed to process outbox event: {}", outboxEvent.getEventId(), e);
        }
    }
}
