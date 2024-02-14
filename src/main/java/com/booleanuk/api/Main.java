package com.booleanuk.api;

import com.booleanuk.api.employee.Employee;
import com.booleanuk.api.employee.EmployeeRepository;
import com.booleanuk.api.department.Department;
import com.booleanuk.api.salarie.Salary;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EmployeeRepository myRepo;
        try {
            myRepo = new EmployeeRepository();
        } catch (Exception e) {
            System.out.println("Initialization failed: " + e);
            return;
        }
        Scanner input = new Scanner(System.in);
        String userChoice;
        do {
            System.out.println("\nChoose an option:");
            System.out.println("A - List all employees");
            System.out.println("B - View an employee");
            System.out.println("C - Update an employee");
            System.out.println("D - Delete an employee");
            System.out.println("E - Add a new employee");
            System.out.println("X - Exit");
            userChoice = input.nextLine().toUpperCase();

            switch (userChoice) {
                case "A":
                    listEmployees(myRepo);
                    break;
                case "B":
                    viewEmployee(myRepo, input);
                    break;
                case "C":
                    updateEmployee(myRepo, input);
                    break;
                case "D":
                    deleteEmployee(myRepo, input);
                    break;
                case "E":
                    addEmployee(myRepo, input);
                    break;
            }
        } while (!userChoice.equals("X"));
    }

    private static void listEmployees(EmployeeRepository repo) {
        try {
            repo.getAll().forEach(System.out::println);
        } catch (Exception e) {
            System.out.println("Error listing employees: " + e.getMessage());
        }
    }

    private static void viewEmployee(EmployeeRepository repo, Scanner input) {
        System.out.println("Enter employee ID:");
        long id = Long.parseLong(input.nextLine());
        try {
            Employee employee = repo.get(id);
            if (employee != null) {
                System.out.println(employee);
            } else {
                System.out.println("Employee not found.");
            }
        } catch (Exception e) {
            System.out.println("Error viewing employee: " + e.getMessage());
        }
    }

    private static void updateEmployee(EmployeeRepository repo, Scanner input) {
        // Similar to addEmployee; fetch the employee first, then update fields
        System.out.println("Update functionality not implemented in this example.");
    }

    private static void deleteEmployee(EmployeeRepository repo, Scanner input) {
        System.out.println("Enter employee ID to delete:");
        long id = Long.parseLong(input.nextLine());
        try {
            boolean success = repo.delete(id);
            if (success) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("Employee deletion failed.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting employee: " + e.getMessage());
        }
    }

    private static void addEmployee(EmployeeRepository repo, Scanner input) {
        System.out.println("Enter employee name:");
        String name = input.nextLine();
        // Placeholder for department and salary; in a real app, you'd fetch these from their repositories
        Department department = new Department(); // Assume this is fetched or created elsewhere
        department.setId(1L); // Example ID
        Salary salary = new Salary(); // Assume this is fetched or created elsewhere
        salary.setId(1L); // Example ID

        Employee employee = new Employee(); // Assume Employee has appropriate constructor or setters
        employee.setName(name);
        employee.setDepartment(department);
        employee.setSalaryGrade(salary);

        try {
            Employee newEmployee = repo.add(employee);
            if (newEmployee != null) {
                System.out.println("New employee added: " + newEmployee);
            } else {
                System.out.println("Failed to add new employee.");
            }
        } catch (Exception e) {
            System.out.println("Error adding new employee: " + e.getMessage());
        }
    }
}
