package com.LetUsCodeTogether.ats.controller;

import com.LetUsCodeTogether.ats.entity.Contest;
import com.LetUsCodeTogether.ats.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/contests")
public class ContestController {

    @Autowired
    private ContestService contestService;

    @GetMapping("/{platform}")
    public List<Contest> getContests(@PathVariable String platform) {
        return contestService.getContests(platform);
    }

}
