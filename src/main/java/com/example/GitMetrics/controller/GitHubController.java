package com.example.GitMetrics.controller;

import com.example.GitMetrics.dto.GitHubResponse;
import com.example.GitMetrics.dto.RepoAnalysisResponse;
import com.example.GitMetrics.service.GitHubService;
import com.example.GitMetrics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("github")
public class GitHubController {

    @Autowired
    private GitHubService gitHubService;
    @Autowired
    private UserService userService;

    @PostMapping("/track")
    public GitHubResponse trackRepo(Authentication authentication, @RequestParam String url) {
        return userService.trackRepoForUser(authentication.getName(), url);
    }

    @GetMapping("/analysis")
    public RepoAnalysisResponse analyze(Authentication authentication, @RequestParam String url) {
        return userService.getRepoAnalysisForUser(authentication.getName(), url);
    }
}