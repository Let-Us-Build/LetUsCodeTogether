package com.LetUsCodeTogether.ats.controller;

import com.LetUsCodeTogether.ats.beans.dto.DailyActivityDto;
import com.LetUsCodeTogether.ats.beans.dto.DailyActivityRequestDto;
import com.LetUsCodeTogether.ats.service.DailyActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class DailyActivityController {

    @Autowired
    private DailyActivityService dailyActivityService;

    @PostMapping("/dailyactivity/getbyyear")
    public List<DailyActivityDto> getDailyActivityByYear(@RequestBody DailyActivityRequestDto dailyActivityRequestDto){
        return dailyActivityService.getActivityByUserIdInYear(dailyActivityRequestDto);
    }

}
