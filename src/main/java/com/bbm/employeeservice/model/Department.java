package com.bbm.employeeservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String shortName;
    private Integer employeeQuantity;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
