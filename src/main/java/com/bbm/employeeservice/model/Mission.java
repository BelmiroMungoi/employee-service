package com.bbm.employeeservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String missionName;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startedAt;

    @DateTimeFormat(iso = DATE, pattern = "dd/MM/yyyy")
    private LocalDate finishedAt;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private MissionStatus missionStatus;

    @ManyToMany(mappedBy = "missions")
    @EqualsAndHashCode.Exclude
    private Set<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }
}
