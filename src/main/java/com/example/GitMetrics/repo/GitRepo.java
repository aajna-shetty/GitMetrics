package com.example.GitMetrics.repo;

import com.example.GitMetrics.model.github;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GitRepo extends JpaRepository<github,Long> {
    Optional<github> findByUrl(String url);
}
