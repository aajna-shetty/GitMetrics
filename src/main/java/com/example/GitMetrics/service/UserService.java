package com.example.GitMetrics.service;

import com.example.GitMetrics.dto.RepoAnalysisResponse;
import com.example.GitMetrics.model.User;
import com.example.GitMetrics.model.github;
import com.example.GitMetrics.model.snap;
import com.example.GitMetrics.repo.GitRepo;
import com.example.GitMetrics.repo.SnapRepo;
import com.example.GitMetrics.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GitRepo gitRepo;

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private SnapRepo snapRepo;

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getEmail());
        }
        return "Fail";
    }

    @Transactional
    public void addRepoToWatchlist(String userEmail, String repoUrl) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        gitHubService.githubresponse(repoUrl);

        github repo = gitRepo.findByUrl(repoUrl)
                .orElseThrow(() -> new RuntimeException("Repo not found after fetch"));

        user.getWatchlists().add(repo); // Fixed: changed from getWatchedRepositories() to getWatchlists()
        userRepo.save(user);
    }

    @Transactional(readOnly = true)
    public List<RepoAnalysisResponse> getUserDashboard(String userEmail) {
        User user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<github> watchlists = user.getWatchlists(); // Fixed: changed from getWatchedRepositories() to getWatchlists()

        return watchlists.stream().map(repo -> {
            List<snap> history = snapRepo.findByGitHubOrderByCapturedAtDesc(repo, PageRequest.of(0, 2));

            int stars = history.isEmpty() ? 0 : history.get(0).getStars();
            int forks = history.isEmpty() ? 0 : history.get(0).getForks();
            int issues = history.isEmpty() ? 0 : history.get(0).getOpenIssues();
            double health = history.isEmpty() ? 0.0 : history.get(0).getHealthScore();

            String trend = analysisService.getGrowthTrend(repo, history);

            return new RepoAnalysisResponse(
                    repo.getRepoName(),
                    repo.getOwner(),
                    stars,
                    forks,
                    issues,
                    health,
                    trend
            );
        }).collect(Collectors.toList());
    }
}