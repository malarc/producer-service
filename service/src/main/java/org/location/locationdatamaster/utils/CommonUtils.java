package org.location.locationdatamaster.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.location.locationdatamaster.models.Location;

@Slf4j
public class CommonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private CommonUtils() {
        log.error("Should not initialize a private utility class");
    }


    public static <T> T getJsonAsObject(String json, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException ex) {
            log.error("error while Deserializing input json {}", json, ex);
            return null;
        }
    }

    public static String convertToJson(Location location) {
        try {
            log.info("converting Location Object to Json {} ", location);
            return OBJECT_MAPPER.writeValueAsString(location);
        } catch (JsonProcessingException e) {
            log.error("Exception convertDeliveryOrderToJson");
        }
        return null;
    }

  }
