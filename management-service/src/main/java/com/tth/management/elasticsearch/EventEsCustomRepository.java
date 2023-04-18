package com.tth.management.elasticsearch;

import com.tth.common.utils.StringUtil;
import com.tth.management.model.Event;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EventEsCustomRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventEsCustomRepository.class);

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateFormatTime = new SimpleDateFormat("yyyy-MM-dd");

    public Page<Event> filterEvent(Integer page, Integer size, boolean isNewest, List<String> eventType, List<String> objectType, List<String> sourceType, String startDate, String endDate){
        try {
            Pageable pageable = PageRequest.of(page, size);
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            nativeSearchQueryBuilder.withPageable(pageable);
            nativeSearchQueryBuilder.withTrackTotalHits(true); // https://www.elastic.co/guide/en/elasticsearch/reference/7.17/search-your-data.html#track-total-hits

            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termQuery("isNewest", isNewest));

            if (eventType != null && !eventType.isEmpty()) {
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                eventType.forEach((dataVendor) -> {
                    boolQuery.should(QueryBuilders.matchPhraseQuery("eventType", dataVendor));
                });

                boolQueryBuilder.must(boolQuery);
            }

            if (objectType != null && !objectType.isEmpty()) {
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                objectType.forEach((dataVendor) -> {
                    boolQuery.should(QueryBuilders.matchPhraseQuery("objectType", dataVendor));
                });

                boolQueryBuilder.must(boolQuery);
            }

            if (sourceType != null && !sourceType.isEmpty()) {
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                sourceType.forEach((dataVendor) -> {
                    boolQuery.should(QueryBuilders.matchPhraseQuery("sourceType", dataVendor));
                });

                boolQueryBuilder.must(boolQuery);
            }

            if (!StringUtil.isNullOrEmpty(startDate) && !StringUtil.isNullOrEmpty(endDate)) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("createdDate").gte(startDate).lte(endDate));
            }

            // withFilter
            nativeSearchQueryBuilder.withFilter(boolQueryBuilder);

            // Sort
            nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort("createdDate").order(SortOrder.DESC));

            NativeSearchQuery searchQuery = nativeSearchQueryBuilder.build();
            LOGGER.info("DSL Query: {}", searchQuery.getQuery() != null ? searchQuery.getQuery().toString() : "");
            LOGGER.info("DSL Filter: {}", searchQuery.getFilter() != null ? searchQuery.getFilter().toString() : "");

//            Query searchQuery = new StringQuery("{\"bool\":{\"must\":[{\"range\":" +
//                    "{\"createdDate\":{\"gte\":\"" + startDate  + "\",\"lte\":\"" + endDate + "\"}}}]" +
//                    ",\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":5,\"sort\":[],\"aggs\":{}");

            List<Event> eventList = new ArrayList<>();
            SearchHits<Object> searchHits = elasticsearchOperations
                    .search(searchQuery,
                            Object.class,
                            IndexCoordinates.of("event"));
            if (searchHits.getTotalHits() <= 0) {
                return new PageImpl<>(eventList, pageable, 0);
            }

            eventList = searchHits.stream().map((s) -> (Event) s.getContent()).collect(Collectors.toList());
            return new PageImpl<>(eventList, pageable, searchHits.getTotalHits());
        } catch (Exception ex) {
            LOGGER.error("ERROR filter vsat media analyzed: ", ex);
            ex.printStackTrace();
        }

        return null;
    }

    private Event transform(Object content) {
        return (Event) content;
    }
}
