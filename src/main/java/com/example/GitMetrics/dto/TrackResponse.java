package com.example.GitMetrics.dto;

public record TrackResponse(
        GitHubResponse githubData,
        String growthMessage
) {}
