package com.LetUsCodeTogether.ats.service;

import com.LetUsCodeTogether.ats.entity.DailyActivity;
import com.LetUsCodeTogether.ats.repo.DailyActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class DailyActivityService {

    @Autowired
    private DailyActivityRepository dailyActivityRepository;

    public DailyActivity addDailyActivity(DailyActivity dailyActivity) {
        try {
            List<DailyActivity> dailyActivities = getDailyActivityByCreatedDateUserIdAndPlatformId(
                    dailyActivity.getCreatedDate(),
                    dailyActivity.getUserId(),
                    dailyActivity.getPlatformId()
            );
    System.out.println(dailyActivity.getCreatedDate());
            boolean isCreatedByScheduler = false;
            boolean isCreatedByUser = false;

            for (DailyActivity activity : dailyActivities) {
                if (activity.getCreatedBy() == 0) {
                    isCreatedByScheduler = true;
                } else {
                    isCreatedByUser = true;
                }
            }

            if (dailyActivity.getUserId() == 0) {
                if (isCreatedByScheduler) {
                    throw new IllegalStateException("Scheduler activity already exists for this user and platform on this day.");
                } else {
                    return dailyActivityRepository.save(dailyActivity);
                }
            } else {
                if (isCreatedByUser) {
                    throw new IllegalStateException("User activity already exists for this user and platform on this day.");
                } else {
                    return dailyActivityRepository.save(dailyActivity);
                }
            }
        } catch (Exception e) {
            System.out.println("Error while adding daily activity: " + e.getMessage() + e);
        }
        return null;
    }


    public List<DailyActivity> getDailyActivityByCreatedDateUserIdAndPlatformId(Calendar activityDate, long userId, Integer platformId) {
        return dailyActivityRepository.findByCreatedDateAndUserIdAndPlatformId(activityDate, userId, platformId);
    }

    public DailyActivity getLatestActivityByUserIdAndPlatformId(long userId, int platformId) {
        return dailyActivityRepository.findTopByUserIdAndPlatformIdOrderByCreatedDateDesc(userId, platformId);
    }

    public DailyActivity getLatestActivity(Long userId) {
        return dailyActivityRepository.findTopByUserIdOrderByCreatedDateDesc(userId);
    }
}
