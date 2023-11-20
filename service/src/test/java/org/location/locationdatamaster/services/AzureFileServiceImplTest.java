package org.location.locationdatamaster.services;

import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.location.locationdatamaster.config.AzureBlobConfig;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { AzureFileServiceImpl.class })
@Import(AzureBlobConfig.class)
@ContextConfiguration(classes =AzureBlobConfig.class)
public class AzureFileServiceImplTest {
    @MockBean
    private BlobContainerAsyncClient containerAsyncClient;

    @MockBean
    private AzureFileService  azureFileUploadService;

    @MockBean
    private BlobServiceAsyncClient blobServiceAsyncClient;

    @InjectMocks
    private AzureBlobConfig azureBlobConfig;

    @MockBean
    private ReactiveKafkaProducerTemplate<String, String> kafkaTemplate;

    private String producerTopic="test";
    @BeforeEach
    public void setUp() {
        BlobServiceAsyncClient blobServiceAsyncClient = new BlobServiceClientBuilder().connectionString(
                "DefaultEndpointsProtocol=https;AccountName=test;AccountKey=test;EndpointSuffix=ignored;BlobEndpoint=https://127.0.0.1:10000")
                .buildAsyncClient();
        this.containerAsyncClient = blobServiceAsyncClient.getBlobContainerAsyncClient("test-container");

        this.azureFileUploadService = new AzureFileServiceImpl(azureBlobConfig,blobServiceAsyncClient, kafkaTemplate);
        azureBlobConfig.setConnectionString("DefaultEndpointsProtocol=https;AccountName=test;AccountKey=test;EndpointSuffix=ignored;BlobEndpoint=https://127.0.0.1:10000");

    }

    @Test
    public void testDownloadFiles() {
        String value = "file123";
        StepVerifier.create(azureFileUploadService.downloadBlobFiles(value)).expectComplete();

    }

    @SneakyThrows
    @Test
    public void testDownloadblob()  {
        Class<?>[] param= new Class<?>[]{String.class};
        Method method= AzureFileServiceImpl.class.getDeclaredMethod("downloadBlob", param);
        method.setAccessible(true);
        String url="http://localhost:8080/download";
        method.invoke(azureFileUploadService,url);
        Class<?>[] params= new Class<?>[]{String.class,String.class};
        Method method1= AzureFileServiceImpl.class.getDeclaredMethod("getBlobUrl", params);
        method1.setAccessible(true);
        String fileName ="file";String type="json";
        method1.invoke(azureFileUploadService,fileName,type);
    }

    @Test
    @SneakyThrows
    public void testPublishToKafka()  {
        //when(kafkaTemplate.send(any(),any(),any())).thenReturn(Mono.empty());

    }




}
