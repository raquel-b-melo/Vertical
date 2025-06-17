package com.rmelo.vertical.shared.utils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class DateValidator {
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    public static boolean isValidDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return date.getDayOfMonth() == Integer.parseInt(dateStr.substring(8, 10));
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null && !startDate.isAfter(endDate);
    }

}