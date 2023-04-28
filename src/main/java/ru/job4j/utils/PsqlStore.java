package ru.job4j.utils;

import ru.job4j.quartz.AlertRabbitSQL;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private Connection cnn;

    public PsqlStore() {
        try (InputStream in = AlertRabbitSQL.class.getClassLoader().
                getResourceAsStream("grabber.properties")) {
            Properties pr = new Properties();
            pr.load(in);
            Class.forName(pr.getProperty("driver-class-name"));
            cnn = DriverManager.getConnection(
                    pr.getProperty("url"),
                    pr.getProperty("username"),
                    pr.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        PsqlStore psqlStore = new PsqlStore();
        psqlStore.save(new Post(
                "Вакансия1",
                "https://career.habr.com/1",
                "Описание: бла...бла...бла",
                LocalDateTime.now())
        );
        psqlStore.save(new Post(
                "Вакансия1",
                "https://career.habr.com/1",
                "Описание: бла...бла...бла",
                LocalDateTime.now())
        );
        psqlStore.save(new Post(
                "Вакансия2",
                "https://career.habr.com/2",
                "Описание: бла...бла...бла",
                LocalDateTime.now())
        );
        var posts = psqlStore.getAll();
        posts.forEach(System.out::println);
        System.out.println(psqlStore.findById(3));
    }

    @Override
    public void save(Post post) throws SQLException {
        try (PreparedStatement ps = cnn.prepareStatement(
                "INSERT INTO post(name, text, link, created) "
                        + "VALUES (?, ?, ?, ?) ON CONFLICT (link) DO NOTHING")) {
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.execute();
        }
    }

    @Override
    public List<Post> getAll() throws SQLException {
        var posts = new ArrayList<Post>();
        try (PreparedStatement ps = cnn.prepareStatement(
                "SELECT * FROM post")) {
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                posts.add(new Post(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("text"),
                        rs.getString("link"),
                        rs.getTimestamp("created").toLocalDateTime()));
            }
        }
        return posts;
    }

    @Override
    public Post findById(int id) throws SQLException {
        Post rsl = new Post();
        try (Statement st = cnn.createStatement()) {
            if (st.execute(String.format("SELECT * FROM post WHERE id = %d", id))) {
                PreparedStatement ps = cnn.prepareStatement(
                        "SELECT * FROM post WHERE id = ?");
                ps.setInt(1, id);
                ResultSet rslSet = ps.executeQuery();
                if (rslSet.next()) {
                    rsl.setId(rslSet.getInt("id"));
                    rsl.setTitle(rslSet.getString("name"));
                    rsl.setDescription(rslSet.getString("text"));
                    rsl.setLink(rslSet.getString("link"));
                    rsl.setCreated(rslSet.getTimestamp("created").toLocalDateTime());
                }
            }
        }
        return rsl;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }
}
