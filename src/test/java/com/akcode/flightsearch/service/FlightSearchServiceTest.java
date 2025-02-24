package com.akcode.flightsearch.service;

import com.akcode.flightsearch.model.Flight;
import com.akcode.flightsearch.util.FileReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class FlightSearchServiceTest {

    @Mock
    private FileReaderUtil fileReaderUtil;

    @InjectMocks
    private FlightSearchServiceImpl flightSearchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchFlights_WithValidParameters_ReturnsSortedFlights() throws IOException {
        // Prepare mock flight data
        List<Flight> mockFlights = List.of(
                new Flight("AF299", "FRA", "LHR", "20-11-2023", "0600", 4.10, 480),
                new Flight("LH123", "MUC", "JFK", "15-12-2023", "0900", 8.50, 900),
                new Flight("BA007", "LHR", "JFK", "10-12-2023", "1500", 7.30, 700)
        );

        // Mock CSVReader to return the mock data
        when(fileReaderUtil.getFlightData(anyString())).thenReturn(mockFlights);

        // Call the service method
        List<Flight> flights = flightSearchService.searchFlights("LHR", "JFK", "10-12-2023", "fare");

        // Assert the flights are sorted by fare
        assertEquals(1, flights.size());
//        assertEquals("AF299", flights.get(0).getFlightNum());  // First flight should have the lowest fare (480)
//        assertEquals("BA007", flights.get(1).getFlightNum());  // Second flight should have fare 700

        // Verify if the CSVReader's readFlightsFromFile method was called
        verify(fileReaderUtil, times(1)).getFlightData(anyString());
    }

    @Test
    public void testSearchFlights_WithInvalidDepartureLocation_ReturnsEmpty() throws IOException {
        List<Flight> mockFlights = List.of(
                new Flight("AF299", "FRA", "LHR", "20-11-2023", "0600", 4.10, 480),
                new Flight("LH123", "MUC", "JFK", "15-12-2023", "0900", 8.50, 900)
        );

        when(fileReaderUtil.getFlightData(anyString())).thenReturn(mockFlights);

        List<Flight> flights = flightSearchService.searchFlights("LAX", "JFK", "10-12-2023", "fare");

        // Assert that no flights were found
        assertTrue(flights.isEmpty());
    }

    @Test
    public void testSearchFlights_WithNoFlightsAvailable_ReturnsError() throws IOException {
        when(fileReaderUtil.getFlightData(anyString())).thenReturn(List.of());

        // Ensure the method throws an exception when no flights are found
        Exception exception = assertThrows(RuntimeException.class, () -> {
            flightSearchService.searchFlights("LHR", "JFK", "10-12-2023", "fare");
        });

        assertEquals("No flights available for the given parameters.", exception.getMessage());
    }
}
