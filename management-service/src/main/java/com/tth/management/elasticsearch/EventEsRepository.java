package com.tth.management.elasticsearch;

import com.tth.management.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventEsRepository extends ElasticsearchRepository<Event, String> {

    Page<Event> findByIsNewestAndEventTypeInAndObjectTypeInAndSourceTypeInAndCreatedDateBetween(Pageable pageable, boolean isNewest, List<String> eventType, List<String> objectType, List<String> sourceType, Date startDate, Date endDate);

//    Event findByParentIdAndIsNewestAndStatusNotIn(String uuid, boolean isNewest, List<String> statusList);

}
