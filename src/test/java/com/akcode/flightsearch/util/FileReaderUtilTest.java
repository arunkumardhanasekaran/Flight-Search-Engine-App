package com.akcode.flightsearch.util;

import com.akcode.flightsearch.exception_handling.GenericException;
import com.akcode.flightsearch.exception_handling.NoFlightsAvailableException;
import com.akcode.flightsearch.model.Flight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class FileReaderUtilTest {

    @InjectMocks
    private FileReaderUtil fileReaderUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFlightData_Success() {
        List<Flight> flights = fileReaderUtil.getFlightData("/flight_data");

        assertNotNull(flights);
        assertEquals(9, flights.size());
    }

    @Test
    void testGetFlightData_DirNotFound() {
        Throwable exception = assertThrows(GenericException.class, () -> fileReaderUtil.getFlightData("/flight_data_1"));

        assertEquals("Flight data file not found", exception.getMessage());
    }
}
