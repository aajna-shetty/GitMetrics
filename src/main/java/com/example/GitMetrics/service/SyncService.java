package com.example.GitMetrics.service;


import com.example.GitMetrics.model.github;
import com.example.GitMetrics.repo.GitRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyncService {

    @Autowired
    private GitRepo gitRepo;
    @Autowired
    private GitHubService gitHubService;

    @Scheduled(cron="0 0 * * * *")
    public void syncData(){
        System.out.println("Sync job started");

        List<github> repos = gitRepo.findAll();
        for(github repo:repos){
            try{
                gitHubService.githubresponse(repo.getUrl());
                System.out.println("Synced: "+repo.getRepoName());
            }catch(Exception e){
                System.out.println("Failed to sync "+repo.getRepoName()+":"+e.getMessage());
            }
        }
        System.out.println("Sync job finished");
    }
}
