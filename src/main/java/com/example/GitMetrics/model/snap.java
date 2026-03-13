package com.example.GitMetrics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
@Data
@Builder
public class snap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stars;
    private int forks;
    private int openIssues;
    private double healthScore;

    @Column(nullable=false)
    private LocalDateTime capturedAt;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="repo_id")
    private github gitHub;
}
