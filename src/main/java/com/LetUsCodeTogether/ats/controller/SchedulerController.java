package com.LetUsCodeTogether.ats.controller;

import com.LetUsCodeTogether.ats.entity.ApiScheduler;
import com.LetUsCodeTogether.ats.service.DynamicSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class SchedulerController {

    @Autowired
    private DynamicSchedulerService dynamicSchedulerService;

    @PostMapping("/scheduler/start")
    public ResponseEntity<String> startScheduler() {
        dynamicSchedulerService.scheduleApis();
        return ResponseEntity.ok("API scheduler started.");
    }

    @PostMapping("/scheduler/cancel/{apiId}")
    public ResponseEntity<String> cancelScheduler(@PathVariable Long apiId) {
        dynamicSchedulerService.cancelScheduledTask(apiId);
        return ResponseEntity.ok("API scheduler cancelled for API ID: " + apiId);
    }

    @PostMapping("/scheduler/reschedule/{apiId}")
    public ResponseEntity<String> rescheduleApi(@PathVariable Long apiId) {
        ApiScheduler apiScheduler = dynamicSchedulerService.getApiSchedulerById(apiId);  // Fetch API details from DB
        if (apiScheduler != null) {
            dynamicSchedulerService.cancelScheduledTask(apiId);
            dynamicSchedulerService.scheduleSingleApi(apiScheduler);
            return ResponseEntity.ok("API rescheduled for API ID: " + apiId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("API not found.");
        }
    }
}

