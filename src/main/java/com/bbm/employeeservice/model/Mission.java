package com.bbm.employeeservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

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

    @CreatedDate
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DATE, pattern = "dd/MM/yyyy hh:mm:ss a")
    private LocalDateTime startedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DATE, pattern = "dd/MM/yyyy")
    private LocalDateTime finishedAt;

    @Enumerated(EnumType.STRING)
    private MissionStatus missionStatus;

    @ManyToMany(mappedBy = "missions")
    private Set<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
