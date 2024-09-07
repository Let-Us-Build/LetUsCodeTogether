package com.LetUsCodeTogether.ats.controller;

import com.LetUsCodeTogether.ats.entity.OverallRank;
import com.LetUsCodeTogether.ats.service.OverallRankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class OverallRankController {
    @Autowired
    private OverallRankService overallRankService;

    @GetMapping("/overallrank/{userId}")
    public OverallRank getPlatformByName(@PathVariable int userId) throws Exception {
        return overallRankService.getOverallRank(userId);
    }
}
