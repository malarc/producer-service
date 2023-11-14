package org.location.locationdatamaster.consumer;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class KafkaConsumerServiceTest {
/*
    @Test
    public void testConsumeReactive() {
        // Mock KafkaReceiver
        KafkaReceiver<String, String> kafkaReceiverMock = mock(KafkaReceiver.class);

        // Create an instance of the service with the mocked KafkaReceiver
        KafkaConsumerService consumerService = new KafkaConsumerService(kafkaReceiverMock);

*//*        // Create a test message
        ReceiverRecord<String, String> testRecord = new ReceiverRecord<>(null, null, null, 0);

        // Set up the mock to return the test message when receive is called
        Mockito.when(kafkaReceiverMock.receive()).thenReturn(Flux.just(testRecord));*//*

        // Trigger the consumeReactive method
        Flux<String> resultFlux = consumerService.consumeReactive();

        // Verify that the KafkaReceiver.receive method was called
        verify(kafkaReceiverMock).receive();

        // Verify that the message processing logic is called
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(consumerService).consumeReactive();
        verify(consumerService).consumeReactive();  // Ensure it was called
        // Add assertions or further verifications based on your specific logic
    }*/
}
