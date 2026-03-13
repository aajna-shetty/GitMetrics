package com.example.GitMetrics.repo;

import com.example.GitMetrics.model.github;
import com.example.GitMetrics.model.snap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface SnapRepo extends JpaRepository<snap,Long> {
    List<snap> findByGitHubOrderByCapturedAtDesc(github gitHub, Pageable pageable);
}
