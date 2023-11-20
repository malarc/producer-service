package org.location.locationdatamaster.config;

import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BlobConnectionConfig {

    private final AzureBlobConfig azureBlobConfig;

    @Bean
    public BlobContainerAsyncClient containerAsyncClient() {
        BlobServiceAsyncClient blobServiceAsyncClient = getBlobServiceAsyncClient();
        return blobServiceAsyncClient.getBlobContainerAsyncClient(azureBlobConfig.getContainerName());
    }

    @Bean
    public BlobServiceAsyncClient getBlobServiceAsyncClient() {
        return new BlobServiceClientBuilder().connectionString(azureBlobConfig.getConnectionString())
                .buildAsyncClient();
    }

    }
