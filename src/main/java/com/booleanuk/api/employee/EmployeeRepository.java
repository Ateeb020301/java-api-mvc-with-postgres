package com.booleanuk.api.employee;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import org.postgresql.ds.PGSimpleDataSource;

public class EmployeeRepository {
    private DataSource dataSource;

    public EmployeeRepository() throws SQLException {
        this.dataSource = createDataSource();
    }

    private DataSource createDataSource() {
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            String dbUser = prop.getProperty("db.user");
            String dbURL = prop.getProperty("db.url");
            String dbPassword = prop.getProperty("db.password");
            String dbDatabase = prop.getProperty("db.database");

            final String url = "jdbc:postgresql://" + dbURL + ":5432/" + dbDatabase + "?user=" + dbUser +"&password=" + dbPassword;
            PGSimpleDataSource dataSource = new PGSimpleDataSource();
            dataSource.setUrl(url);
            return dataSource;
        } catch (Exception e) {
            System.out.println("Oops: " + e);
            return null;
        }
    }

    public Employee add(Employee employee) throws SQLException {
        // This method simplifies the process. You need to handle Department and Salary objects within Employee.
        String SQL = "INSERT INTO employees (name, job_name, department_id, salary_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getJobName());
            // Assuming department and salary objects are fully populated and valid
            statement.setLong(3, employee.getDepartment().getId());
            statement.setLong(4, employee.getSalaryGrade().getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        employee.setId(rs.getLong(1));
                    }
                }
            }
        }
        return employee; // Or handle null differently if employee wasn't added.
    }

    public Employee get(Long id) throws SQLException {
        Employee employee = null;
        String SQL = "SELECT e.*, d.id as department_id, d.name as department_name, s.id as salary_id, s.grade FROM employees e " +
                "JOIN departments d ON e.department_id = d.id " +
                "JOIN salaries s ON e.salary_id = s.id WHERE e.id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee();
                    // Populate employee fields from ResultSet
                }
            }
        }
        return employee; // Or null if not found
    }

    public List<Employee> getAll() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String SQL = "SELECT e.*, d.id as department_id, d.name as department_name, s.id as salary_id, s.grade FROM employees e " +
                "JOIN departments d ON e.department_id = d.id " +
                "JOIN salaries s ON e.salary_id = s.id";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Employee employee = new Employee();
                    // Populate employee fields from ResultSet
                    employees.add(employee);
                }
            }
        }
        return employees;
    }
    public boolean update(Employee employee) throws SQLException {
        String SQL = "UPDATE employees SET name = ?, job_name = ?, department_id = ?, salary_id = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getJobName());
            statement.setLong(3, employee.getDepartment().getId());
            statement.setLong(4, employee.getSalaryGrade().getId());
            statement.setLong(5, employee.getId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }
    public boolean delete(Long id) throws SQLException {
        String SQL = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement statement = conn.prepareStatement(SQL)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }
}
