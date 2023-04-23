package com.tth.id.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tth.common.constant.ResourcePath;
import com.tth.common.message.RequestMessage;
import com.tth.common.message.ResponseMessage;
import com.tth.common.utils.StringUtil;
import com.tth.id.controller.AuthenController;
import com.tth.id.controller.UserController;
import com.tth.id.exception.ValidationException;
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
    private AuthenController authenController;

    @Autowired
    private UserController userController;

    @RabbitListener(queues = "${user.rpc.queue}")
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
                        if ("/user".equalsIgnoreCase(requestPath)) {
                            if(StringUtil.isNullOrEmpty(pathParam)) {
                                response = userController.getAllUsers(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam);
                            } else {
                                response = userController.findUserByUuid(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam, pathParam);
                            }
                        }
                        break;
                    case "POST":
                        if("/user/login".equalsIgnoreCase(requestPath)){
                            response = authenController.userLogin(requestPath, headerParam, bodyParam);
                        }
                        else if("/user".equalsIgnoreCase(requestPath)){
                            response = userController.createUser(request.getRequestPath(), request.getRequestMethod(), headerParam, bodyParam);
                        }
                        else if("/user/authentication".equalsIgnoreCase(requestPath)){
                            response = authenController.authorized(requestPath, headerParam);
                        }
                        break;
                    case "PUT":
                        if("/user".equalsIgnoreCase(requestPath)){
                            response = userController.updateUser(bodyParam, headerParam, pathParam, request.getRequestMethod(), requestPath);
                        } else if("/user/password".equalsIgnoreCase(requestPath)){
                            response = userController.changePassword(bodyParam, headerParam, pathParam, request.getRequestMethod(), requestPath);
                        }
                        break;
                    case "DELETE":
                        if("/user".equalsIgnoreCase(requestPath)){
                            response = userController.deleteUser(requestPath, headerParam, bodyParam, request.getRequestMethod());
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