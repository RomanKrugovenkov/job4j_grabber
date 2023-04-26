package ru.job4j.utils;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    int id;
    String title;
    String link;
    String description;
    LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return title.equals(post.title)
                && link.equals(post.link)
                && description.equals(post.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, link, description);
    }
}
