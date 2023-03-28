package com.tth.management.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaProperties {

    @Value("${kafka.bootstrap.servers}")
    public static String BOOTSTRAP_SERVERS;

    @Value("${kafka.consumer.group}")
    public static String CONSUMER_GROUP;

    @Value("${event.topic.request}")
    public static String EVENT_TOPIC_REQUEST;

    @Value("${kafka.partition}")
    public static int PARTITION;

    @Autowired
    public KafkaProperties(
            @Value("${kafka.bootstrap.servers}") String kafkaBootstrapServers,
            @Value("${kafka.consumer.group}") String kafkaConsumerGroup,
            @Value("${event.topic.request}") String eventTopicRequest,
            @Value("${kafka.partition}") int kafkaPartition) {

        BOOTSTRAP_SERVERS = kafkaBootstrapServers;
        CONSUMER_GROUP = kafkaConsumerGroup;
        EVENT_TOPIC_REQUEST = eventTopicRequest;
        PARTITION = kafkaPartition;

    }

}
