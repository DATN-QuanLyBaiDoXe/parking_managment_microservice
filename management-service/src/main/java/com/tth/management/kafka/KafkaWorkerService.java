package com.tth.management.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.tth.management.model.Event;
import com.tth.management.service.EventService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class KafkaWorkerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaWorkerService.class);

    @Autowired
    private EventService eventService;

    public KafkaWorkerService() {
        LOGGER.info("WorkerServer constructor ...........");
    }

    @KafkaListener(topics = "${event.topic.request}")
    @Async("threadPool")
    public void workerReceive(String json) {
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            Event event = transformEvent(json);
            if(event != null) {
                eventService.save(event);
            }
        } catch (Exception ex) {
            LOGGER.error("Error to handle ViolationEventDTO event >>> {}", ex.getMessage());
        }
    }

    private Event transformEvent(String json) {
        JSONObject jsonObject = new JSONObject(json);
        Event event = new Event();
        event.setPlace(jsonObject.getString("place"));
        event.setImage(jsonObject.getString("image"));
        event.setCreatedDate(new Date());
        return event;
    }

}
