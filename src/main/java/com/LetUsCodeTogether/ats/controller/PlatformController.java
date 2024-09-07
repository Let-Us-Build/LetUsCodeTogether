package com.LetUsCodeTogether.ats.controller;

import com.LetUsCodeTogether.ats.entity.Platform;
import com.LetUsCodeTogether.ats.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class PlatformController {
    @Autowired
    private PlatformService platformService;

    @PostMapping("/platforms/add")
    public Platform addPlatform(@RequestBody Platform platform){
        return platformService.addPlatform(platform);
    }

    @PutMapping("/platforms/update")
    public Platform updatePlatform(@RequestBody Platform platform){
        return platformService.updatePlatform(platform);
    }

    @GetMapping("/platforms/")
    public List<Platform> getAllPlatforms(){
        return platformService.getAllPlatforms();
    }

    @GetMapping("/platforms/{platformName}")
    public List<Platform> getPlatformByName(@PathVariable String platformName){
        return platformService.getPlatformByName(platformName);
    }
}
