package com.tth.vehicle.rabbitmq;

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

    //vehicle service
    @Value("${vehicle.rpc.exchange}")
    public static String VEHICLE_RPC_EXCHANGE;

    @Value("${vehicle.rpc.queue}")
    public static String VEHICLE_RPC_QUEUE;

    @Value("${vehicle.rpc.key}")
    public static String VEHICLE_RPC_KEY;

    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
                              @Value("${user.rpc.queue}") String userRpcQueue,
                              @Value("${user.rpc.key}") String userRpcKey,
                              @Value("${user.rpc.authen.url}") String userRpcAuthenUrl,
                              @Value("${vehicle.rpc.exchange}") String vehicleRpcExchange,
                              @Value("${vehicle.rpc.queue}") String vehicleRpcQueue,
                              @Value("${vehicle.rpc.key}") String vehicleRpcKey){
        //user
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_RPC_AUTHEN_URL = userRpcAuthenUrl;

        //vehicle
        VEHICLE_RPC_EXCHANGE = vehicleRpcExchange;
        VEHICLE_RPC_QUEUE = vehicleRpcQueue;
        VEHICLE_RPC_KEY = vehicleRpcKey;
    }
}
