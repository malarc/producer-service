package org.location.locationdatamaster.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("azure.blob")
@Configuration
public class AzureBlobConfig {
    private String connectionString;
    private String containerName;
    private String blobName;
}
