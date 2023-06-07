package com.dmdev.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class LocalDateFormatterTest {

    static Stream<Arguments> getValidationArguments() {
        return Stream.of(
                Arguments.of("2014-04-12", true),
                Arguments.of("2014-04-12 34", false),
                Arguments.of("01-01-2020", false),
                Arguments.of(null, false),
                Arguments.of("", false)
        );
    }

    @Test
    void format() {
        String date = "2014-04-12";

        LocalDate actualResult = LocalDateFormatter.format(date);

        assertThat(actualResult).isEqualTo(LocalDate.of(2014, 4, 12));
    }

    @Test
    void shouldThrowExceptionIfDateInvalid() {
        String date = "2014-04-12 34";

        assertThrowsExactly(DateTimeParseException.class, () -> LocalDateFormatter.format(date));
    }

    @ParameterizedTest
    @MethodSource("getValidationArguments")
    void isValid(String date, boolean expectedResult) {
        boolean actualResult = LocalDateFormatter.isValid(date);

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}