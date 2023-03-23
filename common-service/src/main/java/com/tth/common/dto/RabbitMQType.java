package com.tth.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RabbitMQType {
    private String path;
    private String method;
    private String rabbit;
}
