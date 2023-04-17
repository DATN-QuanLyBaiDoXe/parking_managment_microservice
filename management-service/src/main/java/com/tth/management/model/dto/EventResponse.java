package com.tth.management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    private String uuid;

    private Integer eventType;

    private String place;

    private Integer objectType;

    private Integer sourceType;

    private String image;

    private Date createdDate;

    private Integer status;

    private String description;
}
