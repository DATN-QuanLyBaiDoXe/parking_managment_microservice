package com.tth.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableElasticsearchRepositories(basePackages = "com.tth.management.elasticsearch")
//@EntityScan(basePackages = "com.tth.management.model")
//@EnableJpaRepositories(basePackages = "com.tth.management.repository")
//@EnableScheduling
public class ReportServiceApplication {

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        SpringApplication.run(ReportServiceApplication.class, args);
    }
}
