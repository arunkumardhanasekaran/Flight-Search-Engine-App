package com.akcode.flightsearch.controller;

import com.akcode.flightsearch.model.Flight;
import com.akcode.flightsearch.service.FlightSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class FlightSearchControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FlightSearchService flightSearchService;

    @InjectMocks
    private FlightSearchController flightSearchController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(flightSearchController).build();
    }

    @Test
    public void testSearchFlights_WithValidParameters_ReturnsFlights() {
        List<Flight> mockFlights = List.of(
                new Flight("AF299", "FRA", "LHR", "20-11-2023", "0600", 4.10, 480),
                new Flight("BA007", "LHR", "JFK", "10-12-2023", "1500", 7.30, 700)
        );

        when(flightSearchService.searchFlights("LHR", "JFK", "10-12-2023", "fare")).thenReturn(mockFlights);

        ResponseEntity<List<Flight>> response = flightSearchController.searchFlights("LHR", "JFK", "10-12-2023", "fare");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("AF299", response.getBody().get(0).getFlightNum());
    }
}
