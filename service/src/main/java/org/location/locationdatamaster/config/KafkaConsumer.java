package org.location.locationdatamaster.config;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class KafkaConsumer {
    @KafkaListener(topics = "apmm.locationref.topic.internal.any.v1", groupId = "group-id")
    public Mono<Void> consume(String message) {
        // Your logic to process the message goes here
        System.out.println("Received message: " + message);
        // Acknowledge the message manually if needed

        return Mono.empty();
    }


}
