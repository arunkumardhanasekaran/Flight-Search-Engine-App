package com.akcode.flightsearch.service;

import com.akcode.flightsearch.exception_handling.LocationNotFoundException;
import com.akcode.flightsearch.exception_handling.NoFlightsAvailableException;
import com.akcode.flightsearch.model.Flight;
import com.akcode.flightsearch.util.FileReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void testSearchFlights_WithValidParameters_ReturnsSortedFlights_ByFare() {
        List<Flight> mockFlights = List.of(
                new Flight("AF299", "FRA", "LHR", "20-11-2023", "0600", 4.10, 480),
                new Flight("LH123", "MUC", "JFK", "15-12-2023", "0900", 8.50, 900),
                new Flight("BA007", "LHR", "JFK", "10-12-2023", "1500", 7.30, 700)
        );

        when(fileReaderUtil.getFlightData(anyString())).thenReturn(mockFlights);

        List<Flight> flights = flightSearchService.searchFlights("LHR", "JFK", "10-12-2023", "fare");

        assertEquals(1, flights.size());
        assertEquals("BA007", flights.get(0).getFlightNum());

        verify(fileReaderUtil, times(1)).getFlightData(anyString());
    }

    @Test
    public void testSearchFlights_WithValidParameters_ReturnsSortedFlights_ByDuration() {
        List<Flight> mockFlights = List.of(
                new Flight("AF299", "FRA", "LHR", "20-11-2023", "0600", 4.10, 480),
                new Flight("LH123", "MUC", "JFK", "15-12-2023", "0900", 8.50, 900),
                new Flight("BA007", "LHR", "JFK", "10-12-2023", "1500", 7.30, 700)
        );

        when(fileReaderUtil.getFlightData(anyString())).thenReturn(mockFlights);

        List<Flight> flights = flightSearchService.searchFlights("LHR", "JFK", "10-12-2023", "duration");

        assertEquals(1, flights.size());
        assertEquals("BA007", flights.get(0).getFlightNum());

        verify(fileReaderUtil, times(1)).getFlightData(anyString());
    }

    @Test
    public void testSearchFlights_WithValidParameters_ReturnsSortedFlights_ByBoth() {
        List<Flight> mockFlights = List.of(
                new Flight("AF299", "FRA", "LHR", "20-11-2023", "0600", 4.10, 480),
                new Flight("LH123", "MUC", "JFK", "15-12-2023", "0900", 8.50, 900),
                new Flight("BA007", "LHR", "JFK", "10-12-2023", "1500", 7.30, 700)
        );

        when(fileReaderUtil.getFlightData(anyString())).thenReturn(mockFlights);

        List<Flight> flights = flightSearchService.searchFlights("LHR", "JFK", "10-12-2023", "both");

        assertEquals(1, flights.size());
        assertEquals("BA007", flights.get(0).getFlightNum());

        verify(fileReaderUtil, times(1)).getFlightData(anyString());
    }

    @Test
    public void testSearchFlights_WithInvalidArrivalLocation() {
        List<Flight> mockFlights = List.of(
                new Flight("AF299", "FRA", "LHR", "20-11-2023", "0600", 4.10, 480),
                new Flight("LH123", "MUC", "JFK", "15-12-2023", "0900", 8.50, 900)
        );

        when(fileReaderUtil.getFlightData(anyString())).thenReturn(mockFlights);

        Throwable exception = assertThrows(LocationNotFoundException.class, () -> flightSearchService.searchFlights("MUC", "FRA", "10-12-2023", "fare"));

        assertEquals("Arrival location does not match.", exception.getMessage());
    }

    @Test
    public void testSearchFlights_WithInvalidDepartureLocation() {
        List<Flight> mockFlights = List.of(
                new Flight("AF299", "FRA", "LHR", "20-11-2023", "0600", 4.10, 480),
                new Flight("LH123", "MUC", "JFK", "15-12-2023", "0900", 8.50, 900)
        );

        when(fileReaderUtil.getFlightData(anyString())).thenReturn(mockFlights);

        Throwable exception = assertThrows(LocationNotFoundException.class, () -> flightSearchService.searchFlights("LAX", "JFK", "10-12-2023", "fare"));

        assertEquals("Departure location does not match.", exception.getMessage());
    }

    @Test
    public void testSearchFlights_WithInvalidLocation() {
        List<Flight> mockFlights = List.of(
                new Flight("AF299", "FRA", "LHR", "20-11-2023", "0600", 4.10, 480),
                new Flight("LH123", "MUC", "JFK", "15-12-2023", "0900", 8.50, 900)
        );

        when(fileReaderUtil.getFlightData(anyString())).thenReturn(mockFlights);

        Throwable exception = assertThrows(LocationNotFoundException.class, () -> flightSearchService.searchFlights("JFK", "LAX", "10-12-2023", "fare"));

        assertEquals("Both Departure and Arrival location does not match.", exception.getMessage());
    }

    @Test
    public void testSearchFlights_WithNoFlightsAvailable_ReturnsError() {
        List<Flight> mockFlights = List.of(
                new Flight("AF299", "FRA", "LHR", "20-11-2023", "0600", 4.10, 480),
                new Flight("LH123", "MUC", "JFK", "15-12-2023", "0900", 8.50, 900)
        );

        when(fileReaderUtil.getFlightData(anyString())).thenReturn(mockFlights);

        Throwable exception = assertThrows(NoFlightsAvailableException.class, () -> flightSearchService.searchFlights("FRA", "LHR", "10-12-2023", "fare"));

        assertEquals("No flights available for the given parameters.", exception.getMessage());
    }
}
