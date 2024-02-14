package com.booleanuk.api.employee;

import com.booleanuk.api.department.Department;
import com.booleanuk.api.salarie.Salary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name="employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "job_name")
    private String jobName;

    @ManyToOne
    @JoinColumn(name = "salary_grade", referencedColumnName = "grade")
    private Salary salaryGrade;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    public Employee(){

    }

    public Employee(String name, String jobName) {
        this.name = name;
        this.jobName = jobName;
    }
}
