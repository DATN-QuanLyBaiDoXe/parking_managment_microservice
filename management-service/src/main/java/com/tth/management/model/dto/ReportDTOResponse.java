package com.tth.management.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTOResponse {

    private String time;
    private List<ReportDTO> reportDTOList;

}
