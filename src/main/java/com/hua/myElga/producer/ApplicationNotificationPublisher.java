package com.hua.myElga.producer;

import com.hua.myElga.config.rabbitmq.RabbitMQNotificationProperties;
import com.hua.myElga.event.ApplicationNotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class ApplicationNotificationPublisher {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationNotificationPublisher.class);

    @Autowired
    private RabbitMQNotificationProperties properties;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public boolean publishEvent(ApplicationNotificationEvent event) {
        CorrelationData correlationData = new CorrelationData(event.eventId().toString());

        rabbitTemplate.convertAndSend(
                properties.exchange(),
                properties.routingKey(),
                event,
                correlationData
        );

        try {
            // Wait for the confirmation result
            CorrelationData.Confirm confirm = correlationData.getFuture().get(10, TimeUnit.SECONDS);

            // Check if the event didn't reach the RabbitMQ
            if (!confirm.isAck()) {
                logger.warn("RabbitMQ could not process event: {}. Reason: {}", event.eventId(), confirm.getReason());

                return false;
            }

            // Check if the event got into the queue
            if (correlationData.getReturned() != null) {
                logger.warn("RabbitMQ could not put the event {} into the queue", event.eventId());

                return false;
            }

            return true;
        } catch (ExecutionException e) {
            logger.error("RabbitMQ confirmation failed for event: {}", event.eventId(), e);

            return false;
        } catch (InterruptedException e) {
            // Restore the interrupted thread
            Thread.currentThread().interrupt();

            logger.warn("RabbitMQ confirmation interrupted for event: {}", event.eventId(), e);

            return false;
        } catch (TimeoutException e) {
            logger.warn("RabbitMQ confirmation timed out for event: {}", event.eventId(), e);

            return false;
        }
    }
}
