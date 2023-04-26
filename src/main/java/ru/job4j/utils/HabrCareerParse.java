package ru.job4j.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) throws IOException {
        HabrCareerParse hcp = new HabrCareerParse(new HabrCareerDateTimeParser());
        System.out.println(hcp.list(PAGE_LINK));
    }

    String retrieveDescription(String link) {
        Connection connection = Jsoup.connect(link);
        Document document;
        try {
            document = connection.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Element elementDesc = document.selectFirst(".vacancy-description__text");
        return elementDesc.text();
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> rsl = new ArrayList<>();
        HabrCareerParse hcp = new HabrCareerParse(dateTimeParser);
        for (int i = 1; i <= 5; i++) {
            Connection connection = Jsoup.connect(String.format("%s%d", PAGE_LINK, i));
            Document document = connection.get();
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String title = titleElement.text();
                String linkVacancy = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String description = hcp.retrieveDescription(linkVacancy);
                String date = row.select(".vacancy-card__date").first().child(0).attr("datetime");
                LocalDateTime dateVacancy = new HabrCareerDateTimeParser().parse(date);
                rsl.add(new Post(title, linkVacancy, description, dateVacancy));
            });
        }
        return rsl;
    }
}
