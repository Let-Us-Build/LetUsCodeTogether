package com.LetUsCodeTogether.ats.controller;

import com.LetUsCodeTogether.ats.entity.Score;
import com.LetUsCodeTogether.ats.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class ScoreController {
    @Autowired
    public ScoreService scoreService;

    @GetMapping("/scores/addorupdate/{userId}")
    public String addOrUpdateScores(@PathVariable int userId) throws Exception {
        return scoreService.addOrUpdateScore(userId);
    }

    @GetMapping("/scores/{userId}/{platformId}")
    private Score getByUserIdAndPlatformId(@PathVariable int userId,
                                                 @PathVariable int platformId) {
        return scoreService.getByUserIdAndPlatformId(userId, platformId);
    }
}
