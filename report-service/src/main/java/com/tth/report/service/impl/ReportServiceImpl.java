package com.tth.report.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tth.common.constant.ResourcePath;
import com.tth.common.message.RequestMessage;
import com.tth.common.message.ResponseMessage;
import com.tth.report.rabbitmq.RabbitMQClient;
import com.tth.report.rabbitmq.RabbitMQProperties;
import com.tth.report.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private static Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Override
    public ResponseMessage reportEvent(String urlParam, Map<String, String> headerParam) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(RabbitMQProperties.REPORT_EVENT_URL);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(urlParam);
        userRpcRequest.setHeaderParam(headerParam);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.MANAGEMENT_RPC_EXCHANGE,
                RabbitMQProperties.MANAGEMENT_RPC_QUEUE, RabbitMQProperties.MANAGEMENT_RPC_KEY, userRpcRequest.toJsonString());
        LOGGER.info("report event - result: " + result);

        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
                return response;
            } catch (JsonProcessingException ex) {
                LOGGER.info("Lỗi parse json khi gọi user service verify: " + ex.toString());
                return null;
            }
        } else {
            //Forbidden
            return null;
        }
    }

    @Override
    public ResponseMessage reportGeneral(String urlParam, Map<String, String> headerParam) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(RabbitMQProperties.REPORT_GENERAL_URL);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(urlParam);
        userRpcRequest.setHeaderParam(headerParam);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.MANAGEMENT_RPC_EXCHANGE,
                RabbitMQProperties.MANAGEMENT_RPC_QUEUE, RabbitMQProperties.MANAGEMENT_RPC_KEY, userRpcRequest.toJsonString());
        LOGGER.info("report event - result: " + result);

        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
                return response;
            } catch (JsonProcessingException ex) {
                LOGGER.info("Lỗi parse json khi gọi user service verify: " + ex.toString());
                return null;
            }
        } else {
            //Forbidden
            return null;
        }
    }

    @Override
    public ResponseMessage reportEventChart(String urlParam, Map<String, String> headerParam) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(RabbitMQProperties.REPORT_EVENT_CHART_URL);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(urlParam);
        userRpcRequest.setHeaderParam(headerParam);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.MANAGEMENT_RPC_EXCHANGE,
                RabbitMQProperties.MANAGEMENT_RPC_QUEUE, RabbitMQProperties.MANAGEMENT_RPC_KEY, userRpcRequest.toJsonString());
        LOGGER.info("report event - result: " + result);

        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
                return response;
            } catch (JsonProcessingException ex) {
                LOGGER.info("Lỗi parse json khi gọi user service verify: " + ex.toString());
                return null;
            }
        } else {
            //Forbidden
            return null;
        }
    }

    @Override
    public ResponseMessage reportEventLine(String urlParam, Map<String, String> headerParam) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(RabbitMQProperties.REPORT_EVENT_LINE_URL);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(urlParam);
        userRpcRequest.setHeaderParam(headerParam);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.MANAGEMENT_RPC_EXCHANGE,
                RabbitMQProperties.MANAGEMENT_RPC_QUEUE, RabbitMQProperties.MANAGEMENT_RPC_KEY, userRpcRequest.toJsonString());
        LOGGER.info("report event - result: " + result);

        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
                return response;
            } catch (JsonProcessingException ex) {
                LOGGER.info("Lỗi parse json khi gọi user service verify: " + ex.toString());
                return null;
            }
        } else {
            //Forbidden
            return null;
        }
    }
}
