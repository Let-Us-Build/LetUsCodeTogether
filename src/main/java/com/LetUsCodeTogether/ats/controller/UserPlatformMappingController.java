package com.LetUsCodeTogether.ats.controller;

import com.LetUsCodeTogether.ats.entity.UserPlatformMapping;
import com.LetUsCodeTogether.ats.service.UserPlatformMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class UserPlatformMappingController {
    @Autowired
    private UserPlatformMappingService userPlatformMappingService;

    @GetMapping("/userplatformmappings/")
    private List<UserPlatformMapping> getAllUserPlatformMappings() {
        return userPlatformMappingService.getAllUserPlatformMappings();
    }

    @PostMapping("/userplatformmappings/addorupdate")
    private List<UserPlatformMapping> addOrUpdateUserPlatformMapping(@RequestBody Map<String, String> userPlatformMapping) {
        return userPlatformMappingService.addOrUpdateUserPlatformMapping(userPlatformMapping);
    }

    @GetMapping("/userplatformmappings/{userId}/{platformId}")
    private UserPlatformMapping getByUserIdAndPlatformId(@PathVariable int userId,
                                                               @PathVariable int platformId) {
        return userPlatformMappingService.getByUserIdAndPlatformId(userId, platformId);
    }
}
