package com.booleanuk.api.department;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentRepository {

    private final DataSource dataSource;

    public DepartmentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Department add(Department department) throws SQLException {
        String sql = "INSERT INTO departments (name, location) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, department.getName());
            statement.setString(2, department.getLocation());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        department.setId(rs.getLong(1));
                    }
                }
                return department;
            }
        }
        return null; // Or throw an exception
    }

    public List<Department> getAll() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Department department = new Department(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("location"));
                departments.add(department);
            }
        }
        return departments;
    }

    public Department get(Long id) throws SQLException {
        String sql = "SELECT * FROM departments WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Department(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("location"));
                }
            }
        }
        return null; // Or throw an exception
    }

    public Department update(Long id, Department department) throws SQLException {
        String sql = "UPDATE departments SET name = ?, location = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, department.getName());
            statement.setString(2, department.getLocation());
            statement.setLong(3, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                return get(id);
            }
        }
        return null; // Or throw an exception
    }

    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM departments WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }
}
