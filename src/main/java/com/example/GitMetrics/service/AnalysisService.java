package com.example.GitMetrics.service;


import com.example.GitMetrics.model.github;
import com.example.GitMetrics.model.snap;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalysisService {
    public double calculateHealthScore(int stars,int forks,int issues){
        return (stars*1.0+forks*2.0)/(issues+1);
    }

    public double calculateGrowthRate(double current,double previous){
        if(previous==0)return 0.0;
        return((current-previous)/previous)*100;
    }

    public String getGrowthTrend(github repo, List<snap> snapshots){
        if(snapshots.size()<2){
            return "Not enough data to calculate growth yet.";
        }

        double currentHealth=snapshots.get(0).getHealthScore();
        double previousHealth = snapshots.get(1).getHealthScore();
        double growth = calculateGrowthRate(currentHealth, previousHealth);

        return String.format("Growth: %.2f%% compared to previous check.",growth);
    }
}
