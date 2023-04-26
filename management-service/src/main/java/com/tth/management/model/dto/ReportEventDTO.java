package com.tth.management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportEventDTO {

    private String time;
    private String code;
    private Integer total;

}
