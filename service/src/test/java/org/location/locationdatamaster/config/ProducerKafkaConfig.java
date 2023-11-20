package org.location.locationdatamaster.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.util.StringUtils;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class ProducerKafkaConfig {

    private final ProducerKafkaProperties producerKafkaProperties;

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> producerTemplate() {
        Map<String, Object> properties = new HashMap<>();
        addDefaultProperties(properties);
        addProducerProperties(properties);

        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(properties));
    }

    private void addDefaultProperties(Map<String, Object> properties) {
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerKafkaProperties.getBootstrapServers());
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, producerKafkaProperties.getClientId());

        // login configuration
        if (StringUtils.hasText(producerKafkaProperties.getUsername())) {
            properties.put("security.protocol", producerKafkaProperties.getSecurityProtocol());
            properties.put("sasl.mechanism", producerKafkaProperties.getSaslMechanism());
            properties.put("sasl.jaas.config", producerKafkaProperties.getLoginModule() + " required username=\""
                    + producerKafkaProperties.getUsername() + "\"" + " password=" + "\""
                    + producerKafkaProperties.getPassword() + "\" ;");
        }

    }

    private void addProducerProperties(Map<String, Object> properties) {
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        ProducerKafkaProperties.Producer producerProperties = producerKafkaProperties.getProducer();
        properties.put(ProducerConfig.LINGER_MS_CONFIG, producerProperties.getLinger());
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, producerProperties.getBatchSize());
        properties.put(ProducerConfig.SEND_BUFFER_CONFIG, producerProperties.getBatchSize());
        properties.put(ProducerConfig.ACKS_CONFIG, producerProperties.getAcksConfig());
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, producerProperties.getTimeout());
        properties.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, producerProperties.getConnectionsMaxIdle());
        properties.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, producerProperties.getMetadataMaxAge());
        properties.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, producerProperties.getMaxRequestSize());
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, producerProperties.getDeliveryTimeout());

    }
}
