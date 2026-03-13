package com.example.GitMetrics.dto;

public record RepoAnalysisResponse(
        String name,
        String owner,
        int stars,
        int forks,
        int issues,
        double healthScore,
        String growthTrend
) {}
