package com.hexagonal.ms_foodcourt.infrastructure.configuration.aws;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Getter
@Setter
public class AwsProperties {

    private String region;
    private String accessKey;
    private String secretKey;
    private Sqs sqs;

    public static class Sqs {
        private String queueUrl;

        public String getQueueUrl() {
            return queueUrl;
        }

        public void setQueueUrl(String queueUrl) {
            this.queueUrl = queueUrl;
        }
    }
}
