package com.example.GitMetrics.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "github_repos")
public class github {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String repoName;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(nullable = false)
    private String owner;

    @OneToMany(mappedBy = "gitHub", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<snap> snapshots = new ArrayList<>();

    @ManyToMany(mappedBy = "watchlists")
    @JsonIgnore
    private Set<User> users = new HashSet<>();
}