package com.LetUsCodeTogether.ats.service;

import com.LetUsCodeTogether.ats.entity.Platform;
import com.LetUsCodeTogether.ats.repo.PlatformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformService {
    @Autowired
    private PlatformRepository platformRepository;

    public Platform addPlatform(Platform platform) {
        platform.setCreatedDate(null);
        return platformRepository.save(platform);
    }

    public List<Platform> getAllPlatforms() {
        return platformRepository.findAll();
    }

    public List<Platform> getPlatformByName(String platformName) {
        return platformRepository.findByPlatformName(platformName);
    }

    public Platform getPlatformById(Integer platformId) {
        return platformRepository.findById(platformId).orElse(null);
    }

    public Platform updatePlatform(Platform platform) {
        Platform existingPlatform = platformRepository.findById(platform.getPlatformId()).orElse(null);
        existingPlatform.setPlatformName(platform.getPlatformName());
        existingPlatform.setPlatformUrl(platform.getPlatformUrl());
        return platformRepository.save(existingPlatform);
    }
}
