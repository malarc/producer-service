package org.location.locationdatamaster.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AlternateCodesTest {

    private AlternateCodes alternateCodes;
    private static final String CODE = "I9XVMBBKTMMHL";
    private static final String CODE_TYPE = "locationId";

    @Test
    public void testAllArgsConstructor() {
        alternateCodes = new AlternateCodes(CODE,CODE_TYPE);
        Assertions.assertEquals(CODE, alternateCodes.getCode(), "Invalid code");
        Assertions.assertEquals(CODE_TYPE, alternateCodes.getCodeType(), "Invalid code type");
    }

    @Test
    public void testNoArgsConstructor() {
        alternateCodes = new AlternateCodes();
        alternateCodes.setCode("I9XVMBBKTMMHL");
        alternateCodes.setCodeType("locationId");
        Assertions.assertEquals(CODE, alternateCodes.getCode(), "Invalid code");
        Assertions.assertEquals(CODE_TYPE, alternateCodes.getCodeType(), "Invalid code type");
    }

    @Test
    public void testBuilder() {
        alternateCodes = AlternateCodes.builder().code(CODE).codeType(CODE_TYPE).build();
        Assertions.assertEquals(CODE, alternateCodes.getCode(), "Invalid code");
        Assertions.assertEquals(CODE_TYPE, alternateCodes.getCodeType(), "Invalid code type");
    }

}
