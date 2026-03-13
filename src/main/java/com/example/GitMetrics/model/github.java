package com.example.GitMetrics.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class github {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String repoName;
    @Column(nullable = false,unique = true)
    private String url;
    @Column(nullable = false)
    private String owner;
    @OneToMany(mappedBy = "gitHub",cascade=CascadeType.ALL,orphanRemoval = true)
    private List<snap> snapshots=new ArrayList<snap>();


}
