package com.hexagonal.ms_foodcourt.infrastructure.output.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexagonal.ms_foodcourt.domain.model.OrderReadyEvent;
import com.hexagonal.ms_foodcourt.domain.spi.ISqsSenderServicePort;
import com.hexagonal.ms_foodcourt.infrastructure.configuration.aws.AwsProperties;
import com.hexagonal.ms_foodcourt.infrastructure.exception.SqsSendException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Service
@RequiredArgsConstructor
public class SqsSenderServiceAdapter implements ISqsSenderServicePort {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final AwsProperties awsProperties;

    @Override
    public void sendMessage(OrderReadyEvent orderReadyEvent) {
        try {
            String json = objectMapper.writeValueAsString(orderReadyEvent);
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(awsProperties.getSqs().getQueueUrl())
                    .messageBody(json)
                    .build());
        } catch (JsonProcessingException | SqsException e) {
            throw new SqsSendException();
        }
    }
}
