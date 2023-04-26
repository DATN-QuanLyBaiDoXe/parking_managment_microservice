package com.tth.report.rabbitmq;

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

    //report service
    @Value("${report.rpc.exchange}")
    public static String REPORT_RPC_EXCHANGE;

    @Value("${report.rpc.queue}")
    public static String REPORT_RPC_QUEUE;

    @Value("${report.rpc.key}")
    public static String REPORT_RPC_KEY;

    @Value("${report.event.url}")
    public static String REPORT_EVENT_URL;

    @Value("${report.general.url}")
    public static String REPORT_GENERAL_URL;

    @Value("${report.event-chart.url}")
    public static String REPORT_EVENT_CHART_URL;

    @Value("${report.event-line.url}")
    public static String REPORT_EVENT_LINE_URL;

    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
                              @Value("${user.rpc.queue}") String userRpcQueue,
                              @Value("${user.rpc.key}") String userRpcKey,
                              @Value("${user.rpc.authen.url}") String userRpcAuthenUrl,
                              @Value("${management.rpc.exchange}") String managementRpcExchange,
                              @Value("${management.rpc.queue}") String managementRpcQueue,
                              @Value("${management.rpc.key}") String managementRpcKey,
                              @Value("${report.rpc.exchange}") String reportRpcExchange,
                              @Value("${report.rpc.queue}") String reportRpcQueue,
                              @Value("${report.rpc.key}") String reportRpcKey,
                              @Value("${report.event.url}") String reportEventUrl,
                              @Value("${report.general.url}") String reportGeneralUrl,
                              @Value("${report.event-chart.url}") String reportEventChartUrl,
                              @Value("${report.event-line.url}") String reportEventLineUrl){
        //user
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_RPC_AUTHEN_URL = userRpcAuthenUrl;

        //management
        MANAGEMENT_RPC_EXCHANGE = managementRpcExchange;
        MANAGEMENT_RPC_QUEUE = managementRpcQueue;
        MANAGEMENT_RPC_KEY = managementRpcKey;

        //report
        REPORT_RPC_EXCHANGE = reportRpcExchange;
        REPORT_RPC_QUEUE = reportRpcQueue;
        REPORT_RPC_KEY = reportRpcKey;

        REPORT_EVENT_URL = reportEventUrl;
        REPORT_GENERAL_URL = reportGeneralUrl;
        REPORT_EVENT_CHART_URL = reportEventChartUrl;
        REPORT_EVENT_LINE_URL = reportEventLineUrl;
    }
}
