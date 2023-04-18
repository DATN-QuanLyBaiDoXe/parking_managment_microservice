package com.tth.management.repository;

import com.tth.management.model.Event;
import com.tth.management.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    Page<Event> findByIsNewestAndEventTypeAndObjectTypeAndSourceTypeAndCreatedDateBetween(Pageable pageable, boolean isNewest, Integer eventType, Integer objectType, Integer sourceType, Date startDate, Date endDate);

    Event findByParentIdAndIsNewestAndStatusNotIn(String parentId, boolean isNewest, List<Status> status);

    List<Event> findByUuidInAndIsNewest(List<String> parentId, boolean isNewest);
}
