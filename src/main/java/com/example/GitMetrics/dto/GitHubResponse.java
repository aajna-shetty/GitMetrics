package com.example.GitMetrics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubResponse(
        String name,
        @JsonProperty("stargazers_count")
        int stars,
        @JsonProperty("forks_count")
        int forks,
        @JsonProperty("open_issues_count")
        int issues,
        Owner owner
) {
    public record Owner(
            String login
    ) {}
}