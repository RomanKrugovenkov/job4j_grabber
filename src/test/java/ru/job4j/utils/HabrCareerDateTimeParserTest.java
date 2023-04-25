package ru.job4j.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

class HabrCareerDateTimeParserTest {

    @Test
    void parseTest() {
        String date = "2023-04-23T17:27:02+03:00";
        HabrCareerDateTimeParser dtp = new HabrCareerDateTimeParser();
        LocalDateTime ldt = dtp.parse(date);
        assertThat(ldt).isEqualTo("2023-04-23T17:27:02");
    }
}