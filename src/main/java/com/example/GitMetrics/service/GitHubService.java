package com.example.GitMetrics.service;

import com.example.GitMetrics.dto.GitHubResponse;
import com.example.GitMetrics.dto.RepoAnalysisResponse;
import com.example.GitMetrics.dto.TrackResponse;
import com.example.GitMetrics.model.github;
import com.example.GitMetrics.model.snap;
import com.example.GitMetrics.repo.GitRepo;
import com.example.GitMetrics.repo.SnapRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GitHubService {
    @Autowired
    private GitRepo gitRepo;
    @Autowired
    private SnapRepo snapRepo;
    @Autowired
    private RestClient restClient;
    @Autowired
    private AnalysisService analysisService;
    public GitHubResponse githubresponse(String url){
        String path = extractPath(url);


        String[] parts = extractPath(url).split("/");
        GitHubResponse response = restClient.get()
                .uri("/repos/{owner}/{repo}", parts[0], parts[1])
                .retrieve()
                .body(GitHubResponse.class);
        if (response==null) return null;

        github gitHub= gitRepo.findByUrl(url)
                .orElseGet(()->{
                                github newRepo = new github();
                                newRepo.setUrl(url);
                                newRepo.setOwner(response.owner().login());
                                newRepo.setRepoName(response.name());
                                newRepo.setName(response.name());
                                return gitRepo.save(newRepo);
                            });


        double health = analysisService.calculateHealthScore(
                response.stars(),
                response.forks(),
                response.issues()
        );
        snap newsnap = snap.builder()
                .stars(response.stars())
                .forks(response.forks())
                .openIssues(response.issues())
                .healthScore(health)
                .capturedAt(LocalDateTime.now())
                .gitHub(gitHub)
                .build();

        snapRepo.save(newsnap);
        List<snap> history = snapRepo.findByGitHubOrderByCapturedAtDesc(gitHub, PageRequest.of(0, 2));
        String trend = analysisService.getGrowthTrend(gitHub, history);

        System.out.println("Trend: " + trend);
        return response;
    }
    private String extractPath(String url) {
        String path = url.replace("https://github.com/", "")
                .replace("http://github.com/", "")
                .replace("github.com/", "");
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        path = path.replaceAll("/$", "");
        String[] parts = path.split("/");
        if (parts.length >= 2) {
            return parts[0] + "/" + parts[1];
        }

        return path;
    }

    public RepoAnalysisResponse getRepoAnalysis(String url) {

        String[] parts = extractPath(url).split("/");

        GitHubResponse response = restClient.get()
                .uri("/repos/{owner}/{repo}", parts[0], parts[1])
                .retrieve()
                .body(GitHubResponse.class);

        github gitHub = gitRepo.findByUrl(url).orElseThrow();

        List<snap> history =
                snapRepo.findByGitHubOrderByCapturedAtDesc(gitHub, PageRequest.of(0, 2));

        double health = analysisService.calculateHealthScore(
                response.stars(), response.forks(), response.issues()
        );

        String trend = analysisService.getGrowthTrend(gitHub, history);

        return new RepoAnalysisResponse(
                response.name(),
                response.owner().login(),
                response.stars(),
                response.forks(),
                response.issues(),
                health,
                trend
        );
    }

}
