package com.example.GitMetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GitMetricsApplication {

	public static void main(String[] args) {

        SpringApplication.run(GitMetricsApplication.class, args);
	}

}
