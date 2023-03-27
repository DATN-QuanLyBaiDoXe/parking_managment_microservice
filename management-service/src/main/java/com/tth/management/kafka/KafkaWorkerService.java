package com.tth.management.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tth.management.model.Event;
import com.tth.management.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class KafkaWorkerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaWorkerService.class);

    @Autowired
    private EventService eventService;

    public KafkaWorkerService() {
        LOGGER.info("WorkerServer constructor ...........");
    }

    @KafkaListener(topics = "${event.topic.request}", groupId = "${kafka.group.id}")
    @Async("threadPool")
    public void workerReceive(String json) {
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            Event event = mapper.readValue(json, Event.class);
            if(event != null) {
                eventService.save(event);
            }
        } catch (Exception ex) {
            LOGGER.error("Error to handle ViolationEventDTO event >>> {}", ex.getMessage());
        }
    }

}
