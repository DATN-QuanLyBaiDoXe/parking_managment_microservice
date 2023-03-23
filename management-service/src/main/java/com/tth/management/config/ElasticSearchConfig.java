package com.tth.management.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {


    @Value("${spring.elasticsearch.host:localhost}")
    private String host;

    @Value("${spring.elasticsearch.port:9200}")
    private int port;

    @Value("${spring.elasticsearch.connectTimeout:60}")
    private int connectTimeout;

    @Value("${spring.elasticsearch.socketTimeout:60}")
    private int socketTimeout;

    @Override
    public RestHighLevelClient elasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .withConnectTimeout(Duration.ofSeconds(connectTimeout))
                .withSocketTimeout(Duration.ofSeconds(socketTimeout))
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @PostConstruct
    public void init() {
//        formatter.setTimeZone(TimeZone.getTimeZone("GMT+7"));
    }

    @Override
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(Arrays.asList(DateToStringConverter.INSTANCE, StringToDateConverter.INSTANCE));
    }

    @WritingConverter
    enum DateToStringConverter implements Converter<Date, String> {
        INSTANCE;
        @Override
        public String convert(Date date) {
            return formatter.format(date);
        }
    }

    @ReadingConverter
    enum StringToDateConverter implements Converter<String, Date> {
        INSTANCE;
        @Override
        public Date convert(String s) {
            try {
                return formatter.parse(s);
            } catch (ParseException e) {
                return null;
            }
        }
    }
//
//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() {
//        return new ElasticsearchRestTemplate(restHighLevelClient());
//    }
//
//    @Bean(destroyMethod = "close")
//    public RestClient restClient() {
//        return restHighLevelClient().getLowLevelClient();
//    }
}