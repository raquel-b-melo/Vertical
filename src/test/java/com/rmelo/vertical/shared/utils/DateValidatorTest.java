package com.rmelo.vertical.shared.utils;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DateValidatorTest {

    @Test
    void shouldReturnTrueForValidDate() {
        assertTrue(DateValidator.isValidDate("2023-05-20"));
    }

    @Test
    void shouldReturnFalseForInvalidDateFormat() {
        assertFalse(DateValidator.isValidDate("20/05/2023")); // Formato inválido
    }

    @Test
    void shouldReturnFalseForNonExistentDate() {
        assertFalse(DateValidator.isValidDate("2023-02-30")); // Data inválida
    }

    @Test
    void shouldReturnTrueForValidDateRange() {
        LocalDate startDate = LocalDate.of(2023, 5, 1);
        LocalDate endDate = LocalDate.of(2023, 5, 20);

        assertTrue(DateValidator.isValidDateRange(startDate, endDate));
    }

    @Test
    void shouldReturnFalseWhenStartDateIsAfterEndDate() {
        LocalDate startDate = LocalDate.of(2023, 5, 21);
        LocalDate endDate = LocalDate.of(2023, 5, 20);

        assertFalse(DateValidator.isValidDateRange(startDate, endDate));
    }

    @Test
    void shouldReturnFalseWhenEitherDateIsNull() {
        LocalDate startDate = null;
        LocalDate endDate = LocalDate.of(2023, 5, 20);

        assertFalse(DateValidator.isValidDateRange(startDate, endDate));
        assertFalse(DateValidator.isValidDateRange(endDate, null));
    }
}