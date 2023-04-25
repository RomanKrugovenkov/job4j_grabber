package ru.job4j.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {

    @Override
    public LocalDateTime parse(String parse) {
        DateTimeFormatter dtf = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(parse, dtf);
    }
}
