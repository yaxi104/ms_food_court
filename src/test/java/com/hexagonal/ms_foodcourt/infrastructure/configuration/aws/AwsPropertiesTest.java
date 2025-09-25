package com.hexagonal.ms_foodcourt.infrastructure.configuration.aws;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class AwsPropertiesTest {

    @Test
    void testGettersAndSetters() {
        AwsProperties awsProperties = new AwsProperties();

        awsProperties.setRegion("us-east-1");
        awsProperties.setAccessKey("my-access-key");
        awsProperties.setSecretKey("my-secret-key");

        AwsProperties.Sqs sqs = new AwsProperties.Sqs();
        sqs.setQueueUrl("https://sqs.us-east-1.amazonaws.com/123456789012/my-queue");

        awsProperties.setSqs(sqs);

        assertEquals("us-east-1", awsProperties.getRegion());
        assertEquals("my-access-key", awsProperties.getAccessKey());
        assertEquals("my-secret-key", awsProperties.getSecretKey());
        assertNotNull(awsProperties.getSqs());
        assertEquals("https://sqs.us-east-1.amazonaws.com/123456789012/my-queue", awsProperties.getSqs().getQueueUrl());
    }
}
