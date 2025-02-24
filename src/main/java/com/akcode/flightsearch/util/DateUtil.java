package com.akcode.flightsearch.util;

import com.akcode.flightsearch.constants.Constants;
import com.akcode.flightsearch.exception_handling.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class DateUtil {

    public static LocalDate parseString(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
            return LocalDate.parse(date, formatter);
        } catch (Exception e) {
            log.error("Invalid date format");
            throw new GenericException("Error occurs while parsing date. (dd-MM-yyyy)");
        }
    }

}
