package org.location.locationdatamaster.services;

import com.azure.storage.blob.BlobAsyncClient;
import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.BlobUrlParts;
import com.azure.storage.blob.models.BlobErrorCode;
import com.azure.storage.blob.models.BlobStorageException;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.location.locationdatamaster.config.AzureBlobConfig;
import org.location.locationdatamaster.models.Location;
import org.location.locationdatamaster.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.*;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzureFileServiceImpl implements AzureFileService {

    private final AzureBlobConfig azureBlobConfig;
    private final BlobServiceAsyncClient blobServiceAsyncClient;
    public static final String JSON = ".json";
    public static final String THE_SPECIFIED_BLOB_DOES_NOT_EXIST = "The specified blob does not exist";
    private final ReactiveKafkaProducerTemplate<String, String> kafkaProducerTemplate;

    @Value("${spring.kafka.producer.topic}")
    private String producerTopic;


    @SneakyThrows
    @Override
    public List<String> getAzureFileList() {
        CloudStorageAccount account = CloudStorageAccount.parse(azureBlobConfig.getConnectionString());
        CloudBlobClient serviceClient = account.createCloudBlobClient();
        CloudBlobContainer container = serviceClient.getContainerReference(azureBlobConfig.getContainerName());
        List azureFileList = new ArrayList<String>();
        container.listBlobs().forEach(blobItem->
        {
          String fullPath = blobItem.getUri().toString();
          String fileNameWithExt = fullPath.substring( fullPath.lastIndexOf('/')+1, fullPath.length() );
          String fileName= fileNameWithExt .substring(0, fileNameWithExt .lastIndexOf('.'));
          log.info("fileName:{}",fileName);
            azureFileList.add(fileName);
        });
        return azureFileList;
    }

    @Override
    public Mono<Location> downloadBlobFiles(String value) {
        return getBlobUrl(value, JSON)
                .flatMap(this::downloadBlob).onErrorResume(BlobStorageException.class, error -> {
                    if (error.getErrorCode().equals(BlobErrorCode.BLOB_NOT_FOUND)) {
                        return Mono.error(new Exception(THE_SPECIFIED_BLOB_DOES_NOT_EXIST));

                    }
                    return Mono.error(new Exception(THE_SPECIFIED_BLOB_DOES_NOT_EXIST));
                }).map(content -> CommonUtils.getJsonAsObject(content,Location.class))
                .flatMap(loc->{
                    String msg = CommonUtils.convertToJson(loc);
                    publishMessageToKafka(UUID.randomUUID()+"_LocationEvent",msg);
                    return Mono.just(loc);
                });
    }

    private void publishMessageToKafka(String key ,String value) {
        log.info("Pushing the message to kafka");
        kafkaProducerTemplate.send(producerTopic, key, value)
                .doOnError(throwable -> log.error("Error while publishing message for LocationEvent{}", value))
                .doOnSuccess(result ->log.info("Successfully sent message to kafka"))
                .subscribe();
    }

    public Mono<String> getBlobUrl(String fileName, String type) {
        try {
            CloudStorageAccount account = CloudStorageAccount.parse(azureBlobConfig.getConnectionString());
            URI blobStorageUri = account.getBlobStorageUri().getPrimaryUri();
            String containerName = azureBlobConfig.getContainerName();
            String blobName = String.format("%s%s", fileName, type);
            return Mono.just(String.format("%s/%s/%s", blobStorageUri, containerName, blobName));
        } catch (Exception ex) {
            log.error("Error in getting  getBlobUrl ", ex);
            return Mono.error(new Exception(ex.getMessage()));
        }
    }

    public Mono<String> downloadBlob(String blobUrl) {
        log.info("downloading the data from blob url{}:", blobUrl);
        BlobUrlParts urlParts = BlobUrlParts.parse(blobUrl);
        BlobContainerAsyncClient containerAsyncClient = blobServiceAsyncClient
                .getBlobContainerAsyncClient(urlParts.getBlobContainerName());
        BlobAsyncClient blobAsyncClient = containerAsyncClient.getBlobAsyncClient(urlParts.getBlobName());
        return blobAsyncClient.downloadStream().map(byteBuffer -> new String(byteBuffer.array())).reduce(String::concat)
                .doOnError(throwable -> log.error("Error while downloading blob content for url{}. Message : {}",
                        blobUrl, throwable));
    }

}
