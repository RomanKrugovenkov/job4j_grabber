package ru.job4j.utils;

import java.sql.SQLException;
import java.util.List;

public interface Store extends AutoCloseable {
    void save(Post post) throws SQLException;

    List<Post> getAll() throws SQLException;

    Post findById(int id) throws SQLException;
}
