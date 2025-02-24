package com.akcode.flightsearch.util;

import com.akcode.flightsearch.constants.Constants;
import com.akcode.flightsearch.exception_handling.GenericException;
import com.akcode.flightsearch.model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DateUtilTest {

    @InjectMocks
    private DateUtil dateUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testParseString_Success() {
        String inputDate = "10-12-2023";

        LocalDate date = dateUtil.parseString(inputDate);

        assertEquals(date.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)), inputDate);
    }

    @Test
    void testParseString_InvalidFormat() {
        Throwable exception = assertThrows(GenericException.class, () -> dateUtil.parseString("111-11-2023"));

        assertEquals("Error occurs while parsing date. (dd-MM-yyyy)", exception.getMessage());
    }
}
