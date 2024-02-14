package com.booleanuk.api.department;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    public Department() {
    }

    public Department(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}
