package com.LetUsCodeTogether.ats.service;

import com.LetUsCodeTogether.ats.beans.dto.DailyActivityDto;
import com.LetUsCodeTogether.ats.beans.dto.DailyActivityRequestDto;
import com.LetUsCodeTogether.ats.entity.DailyActivity;
import com.LetUsCodeTogether.ats.repo.DailyActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
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

    public List<DailyActivityDto> getActivityByUserIdInYear(DailyActivityRequestDto dailyActivityRequestDto){
        List<DailyActivity> dailyActivities = dailyActivityRepository.findAllByUserIdAndYear(dailyActivityRequestDto.getUserId(), dailyActivityRequestDto.getYear());
        List<DailyActivityDto> dailyActivityDtos = new ArrayList<>();
        for(DailyActivity dailyActivity: dailyActivities) {
            DailyActivityDto dailyActivityDto = new DailyActivityDto();
            dailyActivityDto.setUserId(dailyActivity.getUserId());
            dailyActivityDto.setPlatformId(dailyActivity.getPlatformId());
            dailyActivityDto.setDay(dailyActivity.getCreatedDate().getTime());
            dailyActivityDto.setTotalProblemsSolved(dailyActivity.getProblemsSolved());
            dailyActivityDto.setTotalContestsParticipated(dailyActivity.getContestsParticipated());
            dailyActivityDto.setTotalRatings(dailyActivity.getRatings());
            dailyActivityDto.setTotalPoints(dailyActivity.getPoints());
            dailyActivityDto.setTotalScoreDifference(dailyActivity.getScoreDifference());
            dailyActivityDto.setLatestOverallStreakInDays(dailyActivity.getOverallStreakInDays());
            dailyActivityDto.setLatestStreakInDays(dailyActivity.getStreakInDays());
            dailyActivityDtos.add(dailyActivityDto);
        }
        return dailyActivityDtos;
    }
}
