package ru.job4j.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    int id;
    String title;
    String link;
    String description;
    LocalDateTime created;

    public static void main(String[] args) throws IOException {
        String url = "https://career.habr.com/vacancies/1000119703";
        Post post = new Post();
        post.retrieveDescription(url);
        System.out.printf("Описание вакансии:%n%s", post.description);
    }

    void retrieveDescription(String link) throws IOException {
        Connection connection = Jsoup.connect(link);
        Document document = connection.get();
        Element elementDesc = document.selectFirst(".vacancy-description__text");
        description = elementDesc.text();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id && link.equals(post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }
}
