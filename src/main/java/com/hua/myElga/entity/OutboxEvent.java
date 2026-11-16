package com.hua.myElga.entity;

import com.hua.myElga.event.OutboxEventStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    @Column
    private UUID eventId;

    @Enumerated(EnumType.STRING)
    @Column
    private OutboxEventStatus status;

    @Column
    private String event;

    @Column
    private Instant dateOfCreation;

    @Column
    private Instant dateOfPublication;

    public OutboxEvent() {

    }

    public OutboxEvent(UUID id, String event) {
        this.eventId = id;
        this.event = event;
        this.status = OutboxEventStatus.PENDING;
        this.dateOfCreation = Instant.now();
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public OutboxEventStatus getStatus() {
        return status;
    }

    public void setStatus(OutboxEventStatus status) {
        this.status = status;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Instant getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Instant dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Instant getDateOfPublication() {
        return dateOfPublication;
    }

    public void setDateOfPublication(Instant dateOfPublication) {
        this.dateOfPublication = dateOfPublication;
    }
}
