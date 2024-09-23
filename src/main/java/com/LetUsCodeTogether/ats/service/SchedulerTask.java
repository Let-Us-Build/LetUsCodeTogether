package com.LetUsCodeTogether.ats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerTask {

    @Autowired
    private ApiSchedulerService apiSchedulerService;

    @Scheduled(fixedRate = 60000)
    public void runScheduler() {
        apiSchedulerService.executeApis();
    }
}

