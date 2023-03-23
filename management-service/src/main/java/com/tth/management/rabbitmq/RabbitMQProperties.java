package com.tth.management.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQProperties {

    //User service
    @Value("${user.rpc.exchange}")
    public static String USER_RPC_EXCHANGE;

    @Value("${user.rpc.queue}")
    public static String USER_RPC_QUEUE;

    @Value("${user.rpc.key}")
    public static String USER_RPC_KEY;

    @Value("${user.rpc.authen.url}")
    public static String USER_RPC_AUTHEN_URL;

    //management service
    @Value("${management.rpc.exchange}")
    public static String MANAGEMENT_RPC_EXCHANGE;

    @Value("${management.rpc.queue}")
    public static String MANAGEMENT_RPC_QUEUE;

    @Value("${management.rpc.key}")
    public static String MANAGEMENT_RPC_KEY;

    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
                              @Value("${user.rpc.queue}") String userRpcQueue,
                              @Value("${user.rpc.key}") String userRpcKey,
                              @Value("${user.rpc.authen.url}") String userRpcAuthenUrl,
                              @Value("${management.rpc.exchange}") String managementRpcExchange,
                              @Value("${management.rpc.queue}") String managementRpcQueue,
                              @Value("${management.rpc.key}") String managementRpcKey){
        //user
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_RPC_AUTHEN_URL = userRpcAuthenUrl;

        //management
        MANAGEMENT_RPC_EXCHANGE = managementRpcExchange;
        MANAGEMENT_RPC_QUEUE = managementRpcQueue;
        MANAGEMENT_RPC_KEY = managementRpcKey;
    }
}
