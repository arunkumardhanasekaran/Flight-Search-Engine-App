package com.akcode.flightsearch.model;

import com.akcode.flightsearch.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {

    private String flightNum;
    private String depLoc;
    private String arrLoc;
    private String validTill;
    private String flightTime;
    private double flightDurn;
    private double fare;

    public boolean isAvailable(LocalDate flightDate) {
        return !flightDate.isAfter(DateUtil.parseString(validTill));
    }

}
