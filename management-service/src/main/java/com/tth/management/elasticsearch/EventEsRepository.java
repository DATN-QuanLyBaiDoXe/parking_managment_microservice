package com.tth.management.elasticsearch;

import com.tth.management.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventEsRepository extends ElasticsearchRepository<Event, String> {

    @Query("{\"bool\":{\"must\":[{\"range\":{\"createdDate\":{\"gte\":\":startDate\",\"lte\":\":endDate\"}}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":50,\"sort\":[],\"aggs\":{}")
    List<Event> findByIsNewestAndEventTypeInAndObjectTypeInAndSourceTypeInAndCreatedDateGreaterThanEqualAndCreatedDateLessThanEqual
            (@Param("page") Integer page, @Param("size") Integer size, @Param("isNewest") boolean isNewest,
             @Param("eventType") List<String> eventType, @Param("objectType") List<String> objectType,
             @Param("sourceType") List<String> sourceType, @Param("startDate") String startDate, @Param("endDate") String endDate);

//    Event findByParentIdAndIsNewestAndStatusNotIn(String uuid, boolean isNewest, List<String> statusList);

}