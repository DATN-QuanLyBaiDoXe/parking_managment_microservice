package com.tth.management.service;

import com.tth.management.model.Event;
import com.tth.management.model.dto.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface EventService {

    EventPagingDTO getAllEvent(Map<String, Object> bodyParam) throws IOException;

    EventDTO save(Map<String, Object> bodyParam, String uuid);

    EventDTO update(Event event, Map<String, Object> bodyParam, String uuid);

    Event findByUuid(String uuid);

    List<Event> findByUuidIn(List<String> uuids);

    void deleteMultiEvents(List<Event> eventList, String uuid);

    Event findById(String id);

    EventDTO updateStatus(Event event, String uuid, Integer status);

    void addMonthEventPartition(String table);

    List<ReportDTO> reportEventByObject(String filterTimeLevel);

    List<ReportDTO> reportEventByStatus(String filterTimeLevel);

    void save(Event event);

    Long getTotalMoney();

    List<ReportDTOResponse> reportEventChartByObject(String filterTimeLevel) throws ParseException;

    List<ReportDTOResponse> reportEventChartByStatus(String filterTimeLevel) throws ParseException;

    List<ReportEventDTO> reportEventLine() throws ParseException;
}
