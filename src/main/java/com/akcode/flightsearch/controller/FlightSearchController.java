package com.akcode.flightsearch.controller;

import com.akcode.flightsearch.model.Flight;
import com.akcode.flightsearch.service.FlightSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/flights")
public class FlightSearchController {

    @Autowired
    private FlightSearchService flightSearchService;

    @GetMapping("/search")
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam String depLoc,
            @RequestParam String arrLoc,
            @RequestParam String flightDate,
            @RequestParam String outputPreference) {

        log.info("Searching flight between {} and {} on {}", depLoc, arrLoc, flightDate);

        List<Flight> flights = flightSearchService.searchFlights(depLoc, arrLoc, flightDate, outputPreference);

        return ResponseEntity.ok(flights);

    }
}
