package org.location.locationdatamaster.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.location.locationdatamaster.models.KafkaSubmissionResponse;
import org.location.locationdatamaster.models.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final AzureFileService azureFileService;

    @Value("${spring.kafka.producer.topic}")
    private String producerTopic;

/*    @Override
    public Flux<FilePart> downloadAzureFiles(List<String> documentLocation) {
        return Flux.fromIterable(documentLocation).flatMap(value -> azureFileService.downLoadFiles(value));
    }*/

    @Override
    public Flux<Location> downloadBlobFiles(List<String> fileName) {
        return Flux.fromIterable(fileName).flatMap(value -> azureFileService.downloadBlobFiles(value));
    }

   // @Override
/*    public Mono<String> downloadBlobFiles(List<String> fileName) {
        return Flux.fromIterable(fileName).flatMap(value -> azureFileService.downloadBlobFiles(value).flatMap(loc->{
                    String msg = convertLocationObjToJson(loc);
                    ProducerRecord<String, String> producerRecord = createProducerRecord(msg);
                    publishMessageToKafka(producerRecord, kafkaTemplate);

                   return Mono.just("success");
                }

                ));
    }*/

    private <K, V> void publishMessageToKafka(ProducerRecord<K, V> record, KafkaTemplate<K, V> kafkaTemplate) {
        log.info("Pushing the message to kafka");
        // publish to kafka - in a blocking manner for now - to be refactored
        try {
            kafkaTemplate.send(record).get();
        } catch (Exception e) {
            log.error("Exception caught while sending message via Kafka. Exception: ", e);
        }
        log.info("Message was pushed to Kafka");
    }

    private <K, V> ProducerRecord<K, V> createProducerRecord(V location) {
        log.info("Creating kafka record. key: {}, deliveryOrder: {}", location);
        ProducerRecord<K, V> record = new ProducerRecord<>(producerTopic, location);
        record.headers().add(new RecordHeader(KafkaHeaders.MESSAGE_KEY,getUUID()));
        return record;
    }

    private  byte[] getUUID() {
        UUID uuid = UUID.randomUUID();
        long hi = uuid.getMostSignificantBits();
        long lo = uuid.getLeastSignificantBits();
        return ByteBuffer.allocate(16).putLong(hi).putLong(lo).array();
    }

}
