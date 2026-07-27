package com.example.GitMetrics.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    //persist because the change should apply in every table
    //LAZY shouldn't load the db
    //merge , when updated , updates both the table
    @JoinTable(
            name = "user_watchlists",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "repo_id")
    )
    @Builder.Default
    //Otherwise it throws null pointer exception
    private Set<github> watchlists = new HashSet<>();

    public void addRepoToWatchlist(github repo) {
        this.watchlists.add(repo);
    }

    public void removeRepoFromWatchlist(github repo) {
        this.watchlists.remove(repo);
    }
}