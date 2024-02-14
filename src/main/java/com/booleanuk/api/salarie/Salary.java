package com.booleanuk.api.salarie;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "salaries")
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade")
    private String grade;

    @Column(name = "min_salary")
    private Integer minSalary;

    @Column(name = "max_salary")
    private Integer maxSalary;

    public Salary() {
    }

    public Salary(Long id, String grade, Integer minSalary, Integer maxSalary) {
        this.id = id;
        this.grade = grade;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
    }
}
