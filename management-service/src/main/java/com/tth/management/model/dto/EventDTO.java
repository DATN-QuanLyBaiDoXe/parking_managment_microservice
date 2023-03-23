package com.tth.management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    private String uuid;

    private String eventType;

    private String place;

    private String objectType;

    private String sourceType;

    private String image;

    private String parentId;

    private boolean isNewest;

    private boolean isDelete;

    private Date createdDate;

    private String createdBy;

    private Date modifiedDate;

    private String modifiedBy;

    private Integer status;
}
