package com.example.dbschoolproject.courses.dao;

import com.example.dbschoolproject.courses.daoInterface.GroupDao;
import com.example.dbschoolproject.courses.domain.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresSQLGroupDao implements GroupDao {
    public static String PROPERTIES_FILE = null;
    private static String SELECTION_BY_GROUP_ID_QUERY_TEMPLATE = "SELECT groups.group_id, groups.name, COUNT(students.student_id) " +
            "FROM groups " +
            "LEFT JOIN students " +
            "ON students.group_id = groups.group_id " +
            "GROUP BY groups.group_id " +
            "HAVING COUNT(*) <= ? " +
            "ORDER BY groups.group_id";

    public PostgresSQLGroupDao(String PROPERTIES_FILE) {
        this.PROPERTIES_FILE = PROPERTIES_FILE;
    }

    public PostgresSQLGroupDao() {
    }

    @Override
    public void saveAll(List<Group> groups) {
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES_FILE);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.
                     prepareStatement("INSERT INTO groups (group_id, name) VALUES (?, ?)")) {
            for (Group group : groups) {
                statement.setInt(1, group.getId());
                statement.setString(2, group.getName());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Group> findByGroupId(int groupId) {
        List<Group> groups = new ArrayList<>();
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES_FILE);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECTION_BY_GROUP_ID_QUERY_TEMPLATE)) {
            statement.setInt(1, groupId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                groups.add(new Group(resultSet.getInt(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return groups;
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();
        ConnectionFactory connectionFactory = new ConnectionFactory(PROPERTIES_FILE);
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM groups");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                groups.add(new Group(resultSet.getInt(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return groups;
    }
}
