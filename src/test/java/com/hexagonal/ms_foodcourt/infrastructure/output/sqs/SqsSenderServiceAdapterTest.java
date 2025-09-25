package com.hexagonal.ms_foodcourt.infrastructure.output.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexagonal.ms_foodcourt.domain.model.OrderReadyEvent;
import com.hexagonal.ms_foodcourt.infrastructure.configuration.aws.AwsProperties;
import com.hexagonal.ms_foodcourt.infrastructure.exception.SqsSendException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SqsSenderServiceAdapterTest {

    @Mock
    private SqsClient sqsClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AwsProperties awsProperties;

    @InjectMocks
    private SqsSenderServiceAdapter sqsSenderServiceAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        AwsProperties.Sqs sqs = mock(AwsProperties.Sqs.class);
        when(awsProperties.getSqs()).thenReturn(sqs);
        String queueUrl = "https://sqs.us-east-1.amazonaws.com/123456789012/my-queue";
        when(sqs.getQueueUrl()).thenReturn(queueUrl);
    }


    @Test
    void sendMessageSendMessageToSqsTest() throws Exception {
        OrderReadyEvent event = new OrderReadyEvent();
        String json = "{\"orderId\":\"123\"}";

        when(objectMapper.writeValueAsString(event)).thenReturn(json);

        sqsSenderServiceAdapter.sendMessage(event);

        ArgumentCaptor<SendMessageRequest> captor = ArgumentCaptor.forClass(SendMessageRequest.class);
        verify(sqsClient).sendMessage(captor.capture());

        SendMessageRequest request = captor.getValue();
        assertEquals(json, request.messageBody());
    }

    @Test
    void sendMessageThrowSqsSendExceptionOnJsonProcessingExceptionTest() throws Exception {
        OrderReadyEvent event = new OrderReadyEvent();
        when(objectMapper.writeValueAsString(event)).thenThrow(JsonProcessingException.class);
        assertThrows(SqsSendException.class, () -> sqsSenderServiceAdapter.sendMessage(event));
    }

    @Test
    void sendMessageThrowSqsSendExceptionOnSqsExceptionTest() throws Exception {
        OrderReadyEvent event = new OrderReadyEvent();
        String json = "{\"orderId\":\"123\"}";

        when(objectMapper.writeValueAsString(event)).thenReturn(json);
        SqsException sqsException = mock(SqsException.class);
        when(sqsClient.sendMessage(any(SendMessageRequest.class))).thenThrow(sqsException);

        assertThrows(SqsSendException.class, () -> sqsSenderServiceAdapter.sendMessage(event));
    }
}
