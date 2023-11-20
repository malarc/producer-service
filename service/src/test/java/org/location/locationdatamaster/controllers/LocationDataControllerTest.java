package org.location.locationdatamaster.controllers;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.location.locationdatamaster.models.Location;
import org.location.locationdatamaster.services.AzureFileService;
import org.location.locationdatamaster.services.LocationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;



import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = LocationDataController.class)
@ContextConfiguration(classes = { LocationDataController.class })

public class LocationDataControllerTest {
    /*
    //
    //    @MockBean
    //    private LocationService locationService;
    //
    //    @MockBean
    //    private AzureFileService azureFileService;
    //
    //    @Autowired
    //    private WebTestClient webClient;
    //
    //    private LocationDataController locationDataController;
    //
    //    @BeforeEach
    //    void setUp() {
    //        this.locationDataController = new LocationDataController(azureFileService,locationService);
    //    }
    //    private <T> WebTestClient.ResponseSpec createRestReq(T t, HttpMethod http, String path) {
    //        WebTestClient.RequestBodySpec req = webClient.mutateWith(csrf()).method(http).uri(path).contentType(MediaType.APPLICATION_JSON);
    //        if (http != HttpMethod.GET)
    //            req.body(BodyInserters.fromValue(t));
    //
    //        return req.exchange();
    //    }
    //
    //    @Test
    //    void testPublishMessage() {
    //        List<Location> locationList = new ArrayList<>();
    //        locationList.add(Location.builder().status("Active").name("city").build());
    //
    //        List<String> fileList = Arrays.asList("file123","file456","file789");
    //        String publish = "publish" ;
    //        when(locationDataController.getBlobFile(anyString()).then(Mono.just(locationList)));
    //
    //        Flux<Location> response = locationDataController.getBlobFile(publish);
    //        verify(locationService, times(1)).downloadBlobFiles(fileList);
    //        StepVerifier.create(response).expectError().verify();
    //
    //    }
    */
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private LocationService locationService;

    @MockBean
    private AzureFileService azureFileService;

    @InjectMocks
    private LocationDataController locationDataController;

    @BeforeEach
    void setUp() {
        this.locationDataController = new LocationDataController(
                azureFileService,locationService);
    }



    @Test
    void testPublishMessage() {

        List<Location> locationList = new ArrayList<>();
        locationList.add(Location.builder().status("Active").name("city").build());

        List<String> fileList = Arrays.asList("file123", "file456", "file789");
        String publish = "publish";
        Flux<Location> locationFlux = Flux.fromIterable(locationList);
        Mockito.when(azureFileService.getAzureFileList()).thenReturn(fileList);
        Mockito.when(locationService.downloadBlobFiles(fileList)).thenReturn(locationFlux);
        webTestClient.get().uri("/producer/location-data?publish=publish")
                .header(HttpHeaders.ACCEPT, "application/json")
                .exchange()
                .expectStatus().isOk();

    }

}
