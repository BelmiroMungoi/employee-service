package com.bbm.employeeservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String missionName;

    @Column(nullable = false)
    private int duration;

    @ManyToMany(mappedBy = "missions")
    private List<Employee> employees;
}
