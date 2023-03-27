package com.tth.management.service.impl;

import com.tth.management.elasticsearch.EventEsRepository;
import com.tth.management.model.*;
import com.tth.management.model.dto.EventDTO;
import com.tth.management.model.dto.EventPagingDTO;
import com.tth.management.model.dto.ReportDTO;
import com.tth.management.repository.EventCustomizeRepository;
import com.tth.management.repository.EventRepository;
import com.tth.management.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventEsRepository eventEsRepository;

    @Autowired
    private EventCustomizeRepository eventCustomizeRepository;

    @Override
    public EventPagingDTO getAllEvent(Map<String, Object> bodyParam) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer page = bodyParam.get("page") == null ? 0 : (Integer) bodyParam.get("page");
        Integer size = bodyParam.get("size") == null ? 10 : (Integer) bodyParam.get("size");
        List<String> eventType = (List<String>) bodyParam.get("eventType");
        List<String> sourceType = (List<String>) bodyParam.get("sourceType");
        List<String> objectType = (List<String>) bodyParam.get("objectType");
        String start = (String) bodyParam.get("startDate");
        String end = (String) bodyParam.get("endDate");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date startDate = cal.getTime();
        Date endDate = new Date();
        try {
            if(start != null) {
                startDate = format.parse(start);
            }
            if(end != null) {
                endDate = format.parse(end);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Page<Event> result = eventEsRepository.findByIsNewestAndEventTypeInAndObjectTypeInAndSourceTypeInAndCreatedDateBetween(
                PageRequest.of(page, size, Sort.by("createdDate").descending()), true
                ,eventType, objectType, sourceType, startDate, endDate);
        EventPagingDTO dto = new EventPagingDTO(result.getContent(), result.getTotalElements());
        return dto;
    }

    @Override
    public EventDTO save(Map<String, Object> bodyParam, String uuid) {
        Event event = new Event();
        String id = UUID.randomUUID().toString();
        event.setUuid(id);
        event.setEventType(EventType.of((Integer) bodyParam.get("eventType")));
        event.setSourceType(SourceType.MANUAL);
        event.setPlace((String) bodyParam.get("place"));
        event.setObjectType(ObjectType.of((Integer) bodyParam.get("objectType")));
        event.setImage((String) bodyParam.get("image"));
        event.setParentId(id);
        event.setNewest(true);
        event.setCreatedBy(uuid);
        event.setCreatedDate(new Date());
        event.setStatus(Status.of(3));
        eventEsRepository.save(event);
        eventRepository.save(event);
        return transformToEventDTO(event);
    }

    @Override
    public EventDTO update(Event event, Map<String, Object> bodyParam, String uuid) {
        event.setNewest(false);
        Event newEvent = new Event();
        newEvent.setUuid(UUID.randomUUID().toString());
        newEvent.setStatus(event.getStatus());
        newEvent.setNewest(true);
        newEvent.setDelete(false);
        newEvent.setParentId(event.getParentId());
        newEvent.setSourceType(SourceType.MANUAL);
        newEvent.setPlace((String) bodyParam.get("place"));
        newEvent.setImage((String) bodyParam.get("image"));
        newEvent.setEventType(EventType.of((Integer) bodyParam.get("eventType")));
        newEvent.setObjectType(ObjectType.of((Integer) bodyParam.get("objectType")));
        newEvent.setCreatedDate(event.getCreatedDate());
        newEvent.setCreatedBy(event.getCreatedBy());
        newEvent.setModifiedBy(uuid);
        newEvent.setModifiedDate(new Date());
        eventRepository.save(event);
        eventRepository.save(newEvent);
        eventEsRepository.save(event);
        eventEsRepository.save(newEvent);
        return transformToEventDTO(newEvent);
    }

    @Override
    public Event findByUuid(String uuid) {
        return eventRepository.findByParentIdAndIsNewestAndStatusNotIn(uuid, true, List.of(5, 0));
//        return eventEsRepository.findByParentIdAndIsNewestAndStatusNotIn(uuid, true, List.of(5, 0));
    }

    @Override
    public List<Event> findByUuidIn(List<String> uuids) {
        return eventRepository.findByUuidInAndIsNewest(uuids, true);
    }

    @Override
    public void deleteMultiEvents(List<Event> eventList, String uuid) {
        for (Event event : eventList){
            event.setNewest(false);
            event.setDelete(false);
            event.setModifiedBy(uuid);
        }
        eventRepository.saveAll(eventList);
        eventEsRepository.saveAll(eventList);
    }

    @Override
    public Event findById(String id) {
        return eventRepository.findByParentIdAndIsNewestAndStatusNotIn(id, true, List.of(0, 5));
    }

    @Override
    public EventDTO updateStatus(Event event, String uuid, Integer status) {
        event.setNewest(false);
        eventRepository.save(event);
        eventEsRepository.save(event);
        Event newEvent = event;
        newEvent.setStatus(Status.of(status));
        newEvent.setModifiedBy(uuid);
        newEvent.setModifiedDate(new Date());
        eventEsRepository.save(newEvent);
        eventRepository.save(newEvent);
        return transformToEventDTO(newEvent);
    }

    @Override
    public void addMonthEventPartition(String table) {
        eventCustomizeRepository.eventPartitionMonth(table);
    }

    @Override
    public List<ReportDTO> reportEventByObject(String filterTimeLevel) {
        List<ObjectType> objectTypeList = List.of(ObjectType.values());
        List<ReportDTO> reportDTOList = eventCustomizeRepository.reportEventByObject(filterTimeLevel);
        Collection<Integer> allObject = objectTypeList.stream().map(objectType -> objectType.getCode()).collect(Collectors.toList());
        List<Integer> objectInDB = reportDTOList.stream().map(reportDTO -> reportDTO.getCode()).collect(Collectors.toList());
        Collection<Integer> similar = new HashSet<>(allObject);
        Collection<Integer> difference = new HashSet<>();
        difference.addAll(objectInDB);
        difference.addAll(allObject);
        similar.retainAll(objectInDB);
        difference.removeAll(similar);
        if(!difference.isEmpty()) {
            difference.stream().forEach(d -> {
                ReportDTO reportDTO = new ReportDTO();
                reportDTO.setCode(d);
                reportDTO.setName(ObjectType.of(d).getDescription());
                reportDTO.setTotal(0);
                reportDTOList.add(reportDTO);
            });
        }
        reportDTOList.sort(Comparator.comparing(ReportDTO::getCode));
        return reportDTOList;
    }

    @Override
    public List<ReportDTO> reportEventByStatus(String filterTimeLevel) {
        List<Status> statusList = List.of(Status.values());
        List<ReportDTO> reportDTOList = eventCustomizeRepository.reportEventByStatus(filterTimeLevel);
        Collection<Integer> allStatus = statusList.stream().map(status -> status.getCode()).collect(Collectors.toList());
        List<Integer> statusInDB = reportDTOList.stream().map(reportDTO -> reportDTO.getCode()).collect(Collectors.toList());
        Collection<Integer> similar = new HashSet<>(allStatus);
        Collection<Integer> difference = new HashSet<>();
        difference.addAll(statusInDB);
        difference.addAll(allStatus);
        similar.retainAll(statusInDB);
        difference.removeAll(similar);
        if(!difference.isEmpty()) {
            difference.stream().forEach(d -> {
                ReportDTO reportDTO = new ReportDTO();
                reportDTO.setCode(d);
                reportDTO.setName(Status.of(d).getType());
                reportDTO.setTotal(0);
                reportDTOList.add(reportDTO);
            });
        }
        reportDTOList.sort(Comparator.comparing(ReportDTO::getCode));
        return reportDTOList;
    }

    @Override
    public void save(Event event) {
        String id = UUID.randomUUID().toString();
        event.setUuid(id);
        event.setEventType(EventType.of(randomInt(1, 3)));
        event.setObjectType(ObjectType.of(randomInt(0, 4)));
        event.setParentId(id);
        event.setSourceType(SourceType.AUTO);
        event.setNewest(true);
        event.setCreatedBy("AI");
        event.setStatus(Status.of(1));
        eventRepository.save(event);
        eventEsRepository.save(event);
    }

    private static int randomInt(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    private EventDTO transformToEventDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setUuid(event.getUuid());
        eventDTO.setEventType(event.getEventType().getDescription());
        eventDTO.setImage(event.getImage());
        eventDTO.setPlace(event.getPlace());
        eventDTO.setParentId(event.getParentId());
        eventDTO.setObjectType(event.getObjectType().getDescription());
        eventDTO.setSourceType(event.getSourceType().getType());
        eventDTO.setNewest(event.isNewest());
        eventDTO.setDelete(event.isDelete());
        eventDTO.setCreatedBy(event.getCreatedBy());
        eventDTO.setCreatedDate(event.getCreatedDate());
        eventDTO.setModifiedBy(event.getModifiedBy());
        eventDTO.setModifiedDate(event.getModifiedDate());
        eventDTO.setStatus(event.getStatus().getCode());
        return eventDTO;
    }
}
