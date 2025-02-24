package com.akcode.flightsearch.service;

import com.akcode.flightsearch.constants.Constants;
import com.akcode.flightsearch.exception_handling.LocationNotFoundException;
import com.akcode.flightsearch.exception_handling.NoFlightsAvailableException;
import com.akcode.flightsearch.model.Flight;
import com.akcode.flightsearch.util.DateUtil;
import com.akcode.flightsearch.util.FileReaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FlightSearchServiceImpl implements FlightSearchService {

    @Autowired
    private FileReaderUtil fileReaderUtil;

    public List<Flight> searchFlights(String depLoc, String arrLoc, String flightDate, String outputPreference) {

        log.debug("Fetching all flight data");
        List<Flight> allFlights = fileReaderUtil.getFlightData(Constants.CSV_DIR);

        LocalDate searchDate = DateUtil.parseString(flightDate);

        log.debug("Searching flight details based on input parameters");
        List<Flight> filteredFlights = searchFlight(allFlights, depLoc, arrLoc, searchDate);

        log.debug("Sorting flight results");
        sortSearchResults(filteredFlights, outputPreference);

        return filteredFlights;
    }

    private List<Flight> searchFlight(List<Flight> allFlights, String depLoc, String arrLoc, LocalDate searchDate) {
        List<Flight> filteredFlights = new ArrayList<>();

        log.debug("Checking locations available in flight data");
        boolean depLocFound = allFlights.stream().map(Flight::getDepLoc).anyMatch(loc -> loc.equalsIgnoreCase(depLoc));
        boolean arrLocFound = allFlights.stream().map(Flight::getArrLoc).anyMatch(loc -> loc.equalsIgnoreCase(arrLoc));

        if (depLocFound && arrLocFound) {
            filteredFlights = allFlights.stream()
                    .filter(f -> f.getDepLoc().equalsIgnoreCase(depLoc)
                            && f.getArrLoc().equalsIgnoreCase(arrLoc)
                            && f.isAvailable(searchDate))
                    .collect(Collectors.toList());
        } else {
            if (!depLocFound && !arrLocFound) {
                throw new LocationNotFoundException("Both Departure and Arrival location does not match.");
            }
            else if (!depLocFound) {
                throw new LocationNotFoundException("Departure location does not match.");
            } else {
                throw new LocationNotFoundException("Arrival location does not match.");
            }
        }

        if (filteredFlights.isEmpty()) {
            throw new NoFlightsAvailableException("No flights available for the given parameters.");
        }

        return filteredFlights;
    }

    private void sortSearchResults(List<Flight> filteredFlights, String outputPreference) {
        if ("fare".equalsIgnoreCase(outputPreference)) {
            filteredFlights.sort(Comparator.comparingDouble(Flight::getFare));
        } else if ("duration".equalsIgnoreCase(outputPreference)) {
            filteredFlights.sort(Comparator.comparingDouble(Flight::getFlightDurn));
        } else if ("both".equalsIgnoreCase(outputPreference)) {
            filteredFlights.sort(Comparator.comparingDouble(Flight::getFare).thenComparingDouble(Flight::getFlightDurn));
        }
    }
}
