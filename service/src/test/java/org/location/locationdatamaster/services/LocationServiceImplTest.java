package org.location.locationdatamaster.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.location.locationdatamaster.models.Country;
import org.location.locationdatamaster.models.Location;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { LocationServiceImpl.class })
public class LocationServiceImplTest  {

    @MockBean
    private LocationServiceImpl locationService;

    @Test
    @SneakyThrows
    public void testDownloadFiles() {
        List<Location> locationList = new ArrayList<>();
        locationList.add(Location.builder().status("Active").locationId("12345").build());
/*        Class<?>[] param= new Class<?>[]{List.class};
        Method method= LocationServiceImpl.class.getDeclaredMethod("downloadBlobFiles", param);
        method.setAccessible(true);
       List<String> str = Arrays.asList("file1","file2");
        method.invoke(locationService,str);*/
       doReturn(Flux.fromIterable(locationList)).when(locationService).downloadBlobFiles(anyList());
        StepVerifier.create(locationService.downloadBlobFiles(Arrays.asList("file1","file2")))
                .expectNext(Location.builder().status("Active").locationId("12345").build())
                 .verifyComplete();
       verify(locationService, times(1)).downloadBlobFiles(anyList());


    }


}
