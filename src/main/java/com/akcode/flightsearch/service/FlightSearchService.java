package com.akcode.flightsearch.service;

import com.akcode.flightsearch.model.Flight;

import java.util.List;

public interface FlightSearchService {

    List<Flight> searchFlights(String depLoc, String arrLoc, String flightDate, String outputPreference);
}
