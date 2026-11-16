package com.hua.myElga.repository;

import com.hua.myElga.entity.OutboxEvent;
import com.hua.myElga.event.OutboxEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(exported = false)
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findTop10ByStatusOrderByDateOfCreationAsc(OutboxEventStatus status);

}
