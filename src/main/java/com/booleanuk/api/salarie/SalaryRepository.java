package com.booleanuk.api.salarie;

import com.booleanuk.api.salarie.Salary;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaryRepository {

    private final DataSource dataSource;

    public SalaryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Salary add(Salary salary) throws SQLException {
        String sql = "INSERT INTO salaries (grade, min_salary, max_salary) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, salary.getGrade());
            statement.setInt(2, salary.getMinSalary());
            statement.setInt(3, salary.getMaxSalary());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        salary.setId(rs.getLong(1));
                    }
                }
                return salary;
            }
        }
        return null; // Or throw an exception
    }

    public List<Salary> getAll() throws SQLException {
        List<Salary> salaries = new ArrayList<>();
        String sql = "SELECT * FROM salaries";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Salary salary = new Salary(
                        resultSet.getLong("id"),
                        resultSet.getString("grade"),
                        resultSet.getInt("min_salary"),
                        resultSet.getInt("max_salary"));
                salaries.add(salary);
            }
        }
        return salaries;
    }

    public Salary get(Long id) throws SQLException {
        String sql = "SELECT * FROM salaries WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Salary(
                            resultSet.getLong("id"),
                            resultSet.getString("grade"),
                            resultSet.getInt("min_salary"),
                            resultSet.getInt("max_salary"));
                }
            }
        }
        return null; // Or throw an exception
    }

    public Salary update(Long id, Salary salary) throws SQLException {
        String sql = "UPDATE salaries SET grade = ?, min_salary = ?, max_salary = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, salary.getGrade());
            statement.setInt(2, salary.getMinSalary());
            statement.setInt(3, salary.getMaxSalary());
            statement.setLong(4, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                return get(id);
            }
        }
        return null; // Or throw an exception
    }

    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM salaries WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }
}
