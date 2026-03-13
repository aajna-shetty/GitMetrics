package com.example.GitMetrics.controller;

import com.example.GitMetrics.dto.GitHubResponse;
import com.example.GitMetrics.dto.RepoAnalysisResponse;
import com.example.GitMetrics.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    @Autowired
    private GitHubService gitHubService;
    @PostMapping("/track")
    public GitHubResponse trackRepo(@RequestParam String url) {
        return gitHubService.githubresponse(url);
    }

    @GetMapping("/analysis")
    public RepoAnalysisResponse analyze(@RequestParam String url) {
        return gitHubService.getRepoAnalysis(url);
    }
}