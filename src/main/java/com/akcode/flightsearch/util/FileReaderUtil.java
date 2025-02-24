package com.akcode.flightsearch.util;

import com.akcode.flightsearch.exception_handling.GenericException;
import com.akcode.flightsearch.model.Flight;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class FileReaderUtil {

    public List<Flight> getFlightData(String folderPath) {
        List<Flight> flights = new ArrayList<>();

        try {
            File folder = new File(Objects.requireNonNull(this.getClass().getResource(folderPath)).toURI());

            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            List<Future<List<Flight>>> futures = new ArrayList<>();

            for (File csvFile : Objects.requireNonNull(folder.listFiles())) {
                futures.add(executorService.submit(() -> parseFlightDetails(csvFile)));
            }

            executorService.shutdown();

            for (Future<List<Flight>> future : futures) {
                flights.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException | URISyntaxException e) {
            throw new GenericException("Error occurs while reading data file.");
        } catch (NullPointerException ne) {
            throw new GenericException("Flight data file not found");
        }

        return flights;
    }

    private List<Flight> parseFlightDetails(File csvFile) throws IOException {
        List<Flight> flights = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csvFile.toPath())) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split("\\|");
                if (values.length == 7) {
                    flights.add(new Flight(values[0], values[1], values[2], values[3], values[4],
                            Double.parseDouble(values[5]), Double.parseDouble(values[6])));
                }
            }
        }
        return flights;
    }
}
