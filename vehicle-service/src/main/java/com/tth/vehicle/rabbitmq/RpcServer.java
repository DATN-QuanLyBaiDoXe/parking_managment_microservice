package com.tth.vehicle.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tth.common.constant.ResourcePath;
import com.tth.common.message.RequestMessage;
import com.tth.common.message.ResponseMessage;
import com.tth.common.utils.StringUtil;
import com.tth.vehicle.controller.OwnerController;
import com.tth.vehicle.controller.VehicleController;
import com.tth.vehicle.exception.ValidationException;
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
    private OwnerController ownerController;

    @Autowired
    private VehicleController vehicleController;

    @RabbitListener(queues = "${vehicle.rpc.queue}")
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
                        if ("/vehicle/owner".equalsIgnoreCase(requestPath)) {
                            if(StringUtil.isNullOrEmpty(pathParam)){
                                response = ownerController.getAllOwners(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam);
                            } else {
                                response = vehicleController.getVehicleByOwner(request.getRequestPath(), request.getRequestMethod(), pathParam, headerParam);
                            }
                        } else if ("/vehicle".equalsIgnoreCase(requestPath)) {
                            response = vehicleController.getAllVehicles(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam);
                        }
                        break;
                    case "POST":
                        if("/vehicle/owner".equalsIgnoreCase(requestPath)){
                            response = ownerController.createOwner(requestPath, headerParam, bodyParam);
                        }
                        else if("/vehicle".equalsIgnoreCase(requestPath)){
                            response = vehicleController.createVehicle(request.getRequestPath(), request.getRequestMethod(), headerParam, bodyParam);
                        }
//                        else if("/user/authentication".equalsIgnoreCase(requestPath)){
//                            response = authenController.authorized(requestPath, headerParam);
//                        }
                        break;
                    case "PUT":
                        if("/vehicle/owner".equalsIgnoreCase(requestPath)){
                            response = ownerController.updateOwner(bodyParam, headerParam, pathParam, request.getRequestMethod(), requestPath);
                        } else if("/vehicle".equalsIgnoreCase(requestPath)){
                            response = vehicleController.updateVehicle(bodyParam, headerParam, pathParam, request.getRequestMethod(), requestPath);
                        }
                        break;
                    case "DELETE":
                        if("/vehicle/owner".equalsIgnoreCase(requestPath)){
                            response = ownerController.deleteOwner(requestPath, headerParam, bodyParam, request.getRequestMethod());
                        } else if("/vehicle".equalsIgnoreCase(requestPath)){
                            response = vehicleController.deleteVehicle(requestPath, headerParam, bodyParam, request.getRequestMethod());
                        }
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