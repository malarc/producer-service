package org.location.locationdatamaster.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.location.locationdatamaster.models.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final AzureFileService azureFileService;

    @Override
    public Flux<Location> downloadBlobFiles(List<String> fileName) {
        return Flux.fromIterable(fileName).flatMap(value -> azureFileService.downloadBlobFiles(value));
    }

}
