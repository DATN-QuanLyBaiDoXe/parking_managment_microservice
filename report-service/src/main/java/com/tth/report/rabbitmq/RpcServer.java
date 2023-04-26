package com.tth.report.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tth.common.constant.ResourcePath;
import com.tth.common.message.RequestMessage;
import com.tth.common.message.ResponseMessage;
import com.tth.common.utils.StringUtil;
import com.tth.report.controller.ReportController;
import com.tth.report.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

public class RpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    @Autowired
    private ReportController reportController;

    @RabbitListener(queues = "${report.rpc.queue}")
    public String processService(String json) throws ValidationException {
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);

            //Process here
            ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            if (request != null) {
                String requestPath = request.getRequestPath().replace(request.getVersion() != null
                        ? request.getVersion() : ResourcePath.VERSION, "");
                String urlParam = request.getUrlParam();
                String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();
                //GatewayDebugUtil.debug(requestPath, urlParam, pathParam, bodyParam, headerParam);
                LOGGER.info(" [-->] Server received requestPath =========>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + requestPath);

                switch (request.getRequestMethod()) {
                    case "GET":
                        if ("/report".equalsIgnoreCase(requestPath)) {
                            response = reportController.reportEvent(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam);
                        } else if ("/report/money".equalsIgnoreCase(requestPath)) {
                            response = reportController.reportGeneral(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam);
                        } else if ("/report/chart".equalsIgnoreCase(requestPath)) {
                            response = reportController.reportEventChart(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam);
                        } else if ("/report/line".equalsIgnoreCase(requestPath)) {
                            response = reportController.reportEventLine(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam);
                        }
                        break;
                    case "POST":
//                        if("/management".equalsIgnoreCase(requestPath)){
//                            response = eventController.getAllEvent(requestPath, headerParam, bodyParam);
//                        }
//                        else if("/management/event".equalsIgnoreCase(requestPath)){
//                            response = eventController.createEvent(request.getRequestPath(), headerParam, bodyParam);
//                        }
//                        else if("/user/authentication".equalsIgnoreCase(requestPath)){
//                            response = authenController.authorized(requestPath, headerParam);
//                        }
                        break;
                    case "PUT":
//                        if("/management/event".equalsIgnoreCase(requestPath)){
//                            response = eventController.updateEvent(bodyParam, headerParam, pathParam, request.getRequestMethod(), requestPath);
//                        } else if("/management/update-status".equalsIgnoreCase(requestPath)){
//                            response = eventController.updateStatus(bodyParam, headerParam, pathParam, request.getRequestMethod(), requestPath);
//                        }
                        break;
                    case "DELETE":
//                        if("/management/event".equalsIgnoreCase(requestPath)){
//                            response = eventController.deleteEvent(requestPath, headerParam, bodyParam, request.getRequestMethod());
////                        } else if("/vehicle".equalsIgnoreCase(requestPath)){
////                            response = vehicleController.deleteVehicle(requestPath, headerParam, bodyParam, request.getRequestMethod());
//                        }
                        break;
                    default:
                        break;
                }
            }
            LOGGER.info(" [<--] Server returned " + response != null ? response.toJsonString() : "null");
            return response != null ? response.toJsonString() : null;
        } catch (Exception ex) {
            LOGGER.error("Error to processService >>> " + StringUtil.printException(ex));
            ex.printStackTrace();
        }
        return null;
    }
}