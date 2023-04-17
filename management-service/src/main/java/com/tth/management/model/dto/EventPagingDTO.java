package com.tth.management.model.dto;

import com.tth.management.model.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventPagingDTO {
    List<EventResponse> eventList;
    Long total;
}
