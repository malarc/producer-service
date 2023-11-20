package org.location.locationdatamaster.controllers;


import com.azure.core.http.rest.PagedIterable;
import com.azure.core.http.rest.PagedResponse;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.ListBlobsOptions;
import com.microsoft.azure.storage.StorageException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.location.locationdatamaster.config.AzureBlobConfig;

import org.location.locationdatamaster.models.Location;
import org.location.locationdatamaster.services.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.location.locationdatamaster.services.AzureFileService;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RestController
@Validated
@AllArgsConstructor
public class LocationDataController {


    private  AzureFileService azureFileService;
    private LocationService locationService;

    @GetMapping(path = "/producer/location-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Location> downloadBlobFiles(@RequestParam(value = "publish") String publish){
        List<String> fileList = azureFileService.getAzureFileList();
        return locationService.downloadBlobFiles(fileList);
    }

}


