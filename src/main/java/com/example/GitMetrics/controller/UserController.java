package com.example.GitMetrics.controller;

import com.example.GitMetrics.dto.RepoAnalysisResponse;
import com.example.GitMetrics.model.User;
import com.example.GitMetrics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/github/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String token = userService.verify(user);
        if ("Fail".equals(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/watchlist")
    public ResponseEntity<?> addToWatchlist(Authentication authentication, @RequestParam String url) {
        String userEmail = authentication.getName(); // Extracted from JWT automatically
        userService.addRepoToWatchlist(userEmail, url);
        return ResponseEntity.ok(Map.of("message", "Repository added to watchlist successfully."));
    }

    @GetMapping("/watchlist")
    public ResponseEntity<List<RepoAnalysisResponse>> getWatchlist(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserDashboard(authentication.getName()));
    }

    @DeleteMapping("/watchlist")
    public ResponseEntity<?> removeFromWatchlist(Authentication authentication, @RequestParam String url) {
        userService.removeRepoFromWatchlist(authentication.getName(), url);
        return ResponseEntity.ok(Map.of("message", "Repository removed from watchlist successfully."));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<RepoAnalysisResponse>> getDashboard(Authentication authentication) {
        String userEmail = authentication.getName(); // Extracted from JWT automatically
        return ResponseEntity.ok(userService.getUserDashboard(userEmail));
    }
}