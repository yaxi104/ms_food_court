package com.hexagonal.ms_foodcourt.infrastructure.configuration.aws;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@Profile("test")
public class AwsTestConfig {

    @Bean
    public SqsClient sqsClient() {
        return Mockito.mock(SqsClient.class);
    }
}
