package org.location.locationdatamaster.services;
import com.microsoft.azure.storage.StorageException;
import org.location.locationdatamaster.models.Location;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.List;

public interface AzureFileService {

    List<String> getAzureFileList();

    Mono<Location> downloadBlobFiles(String value);

}
