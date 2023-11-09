package org.location.locationdatamaster.services;

import org.location.locationdatamaster.models.Location;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;

import java.net.URL;
import java.util.List;

public interface LocationService {

   //Flux<FilePart> downloadAzureFiles(List<String> documentLocation);

   Flux<Location> downloadBlobFiles(List<String> fileName);
}

