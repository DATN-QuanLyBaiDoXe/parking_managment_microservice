package com.tth.management.schedule;

import com.tth.management.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class Schedule {

    @Autowired
    private EventService eventService;

    @Scheduled(cron = "0 0 0 01 * *")
    public void partitionEvent(){
        String table = "event";
        eventService.addMonthEventPartition(table);
    }
}
