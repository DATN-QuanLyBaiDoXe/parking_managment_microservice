package com.tth.id.interceptor;

import com.tth.common.utils.StringUtil;
import com.tth.id.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class RequestResponseHandlerInterceptor implements ClientHttpRequestInterceptor {

    private static final String AUTHORIZATION = "Authorization";

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseHandlerInterceptor.class);

    @Autowired
    private TokenService tokenService;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        //Add Bearer Token 2 Header
        String accessToken = tokenService.getAccessToken();
        LOGGER.info("DBM request accessToken: {}", accessToken);
        request.getHeaders().add(AUTHORIZATION, accessToken);
        //Trace request
        //traceRequest(request, body);
        //Execute
        ClientHttpResponse response = execution.execute(request, body);
        LOGGER.info("DBM response status code: {}", response.getStatusCode());
        //Process if accessToken expired
        if (HttpStatus.UNAUTHORIZED == response.getStatusCode()) {
            tokenService.removeAccessToken();
            accessToken = tokenService.getAccessToken();
            if (!StringUtil.isNullOrEmpty(accessToken)) {
                request.getHeaders().remove(AUTHORIZATION);
                request.getHeaders().add(AUTHORIZATION, accessToken);
                LOGGER.info("DBM refresh request accessToken: {}", accessToken);
                //retry
                response = execution.execute(request, body);
            }
        }
        //Trace response
        //traceResponse(response);
        return response;
    }
}
