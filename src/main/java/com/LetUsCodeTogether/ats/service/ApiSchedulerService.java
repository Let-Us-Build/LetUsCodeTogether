package com.LetUsCodeTogether.ats.service;

import com.LetUsCodeTogether.ats.entity.ApiScheduler;
import com.LetUsCodeTogether.ats.repo.ApiSchedulerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApiSchedulerService {

    @Autowired
    private ApiSchedulerRepository apiSchedulerRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public void executeApis() {
        List<ApiScheduler> apiSchedulers = apiSchedulerRepository.findAll();

        for (ApiScheduler apiScheduler : apiSchedulers) {
            new Thread(() -> triggerApi(apiScheduler)).start();
        }
    }

    void triggerApi(ApiScheduler apiScheduler) {
        String url = apiScheduler.getApiUrl();
        String payload = apiScheduler.getPayload();

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(payload, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Response from API: " + response.getBody());
            apiScheduler.setLastExecution(LocalDateTime.now());
            apiSchedulerRepository.save(apiScheduler);
        } catch (Exception e) {
            System.err.println("Error while executing API: " + e.getMessage());
        }
    }
}

