package com.LetUsCodeTogether.ats.service;

import com.LetUsCodeTogether.ats.entity.ApiScheduler;
import com.LetUsCodeTogether.ats.repo.ApiSchedulerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class DynamicSchedulerService {

    @Autowired
    private ApiSchedulerRepository apiSchedulerRepository;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private ApiSchedulerService apiSchedulerService;

    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public void scheduleApis() {
        List<ApiScheduler> apiSchedulers = apiSchedulerRepository.findAll();

        for (ApiScheduler apiScheduler : apiSchedulers) {
            scheduleSingleApi(apiScheduler);
        }
    }

    public void scheduleSingleApi(ApiScheduler apiScheduler) {
        String cronExpression = apiScheduler.getCronExpression();

        CronTrigger cronTrigger = new CronTrigger(cronExpression);
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(() -> {
            apiSchedulerService.triggerApi(apiScheduler);
        }, cronTrigger);
        scheduledTasks.put(apiScheduler.getId(), scheduledTask);
    }

    public void cancelScheduledTask(Long apiId) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(apiId);
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(false);
            scheduledTasks.remove(apiId);
        }
    }

    public ApiScheduler getApiSchedulerById(Long apiId) {
        return apiSchedulerRepository.findById(apiId).orElse(null);
    }

}


