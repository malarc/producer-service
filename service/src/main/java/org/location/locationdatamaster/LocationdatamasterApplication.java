package org.location.locationdatamaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.List;

@SpringBootApplication
public class LocationdatamasterApplication {

	public static void main(String[] args) {

/*		Flux.fromIterable(List.of("a","b")).subscribe(name -> {
			System.out.println(name);
		});*/

		SpringApplication.run(LocationdatamasterApplication.class, args);
	}

}
