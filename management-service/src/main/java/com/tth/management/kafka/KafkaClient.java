package com.tth.management.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
public class KafkaClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaClient.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public boolean callKafkaServerWorker(String topicRequest, String msg) {
        LOGGER.info("Call Worker Kafka Server Topic: {}, msg: {}", topicRequest, msg);
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topicRequest, msg);
            ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(record);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
