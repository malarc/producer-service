package org.location.locationdatamaster.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AlternateNamesTest {

    private AlternateNames alternateNames;
    private static final String NAME = "City";
    private static final String STATUS = "Active";
    private static final String DESCRIPTION = "LocationId";

    @Test
    public void testAllArgsConstructor() {
        alternateNames = new AlternateNames(NAME,STATUS,DESCRIPTION);
        Assertions.assertEquals(NAME, alternateNames.getName(), "Invalid name");
        Assertions.assertEquals(STATUS, alternateNames.getStatus(), "Invalid status");
        Assertions.assertEquals(DESCRIPTION, alternateNames.getDescription(), "Invalid description");
    }

    @Test
    public void testNoArgsConstructor() {
        alternateNames = new AlternateNames();
        alternateNames.setName(NAME);
        alternateNames.setStatus(STATUS);
        alternateNames.setDescription(DESCRIPTION);
        Assertions.assertEquals(NAME, alternateNames.getName(), "Invalid name");
        Assertions.assertEquals(STATUS, alternateNames.getStatus(), "Invalid status");
        Assertions.assertEquals(DESCRIPTION, alternateNames.getDescription(), "Invalid description");
    }

    @Test
    public void testBuilder() {
        alternateNames = AlternateNames.builder().name(NAME).status(STATUS).description(DESCRIPTION).build();
        Assertions.assertEquals(NAME, alternateNames.getName(), "Invalid name");
        Assertions.assertEquals(STATUS, alternateNames.getStatus(), "Invalid status");
        Assertions.assertEquals(DESCRIPTION, alternateNames.getDescription(), "Invalid description");
    }


}
