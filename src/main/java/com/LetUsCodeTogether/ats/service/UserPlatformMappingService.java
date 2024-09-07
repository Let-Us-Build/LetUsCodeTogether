package com.LetUsCodeTogether.ats.service;

import com.LetUsCodeTogether.ats.entity.UserPlatformMapping;
import com.LetUsCodeTogether.ats.repo.UserPlatformMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserPlatformMappingService {

    @Autowired
    private UserPlatformMappingRepository userPlatformMappingRepository;

    public List<UserPlatformMapping> getAllUserPlatformMappings(){
        return userPlatformMappingRepository.findAll(Sort.by(Sort.Direction.ASC, "userId"));
    }

    public List<UserPlatformMapping> addOrUpdateUserPlatformMapping(Map<String, String> allUserPlatformMappings) {
        List<UserPlatformMapping> userPlatformMappings = new ArrayList<>();
        Set<String> processedPlatforms = new HashSet<>();
        for (Map.Entry<String, String> entry : allUserPlatformMappings.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.endsWith("Username") || key.endsWith("PlatformId")) {
                String platform = key.replace("Username", "")
                                        .replace("PlatformId", "");
                if (!processedPlatforms.contains(platform)) {
                    UserPlatformMapping userPlatformMapping = new UserPlatformMapping();
                    userPlatformMapping.setPlatformId(Integer.valueOf(allUserPlatformMappings.get(platform + "PlatformId")));
                    userPlatformMapping.setUsernameOnPlatform(allUserPlatformMappings.get(platform + "Username"));
                    userPlatformMapping.setUserId(Integer.valueOf(allUserPlatformMappings.get("userId")));
                    userPlatformMapping = addOrUpdateUserPlatformMappingHelper(userPlatformMapping);
                    userPlatformMappings.add(userPlatformMapping);
                    processedPlatforms.add(platform);
                }
            }
        }
        return userPlatformMappings;
    }


    public UserPlatformMapping addOrUpdateUserPlatformMappingHelper(UserPlatformMapping userPlatformMapping) {
        UserPlatformMapping existingUserPlatformMapping = userPlatformMappingRepository.
                findByUserIdAndPlatformId(userPlatformMapping.getUserId(), userPlatformMapping.getPlatformId()).orElse(null);
        if(existingUserPlatformMapping==null) {
            userPlatformMapping.setCreatedDate(Calendar.getInstance());
            return userPlatformMappingRepository.save(userPlatformMapping);
        }
        existingUserPlatformMapping.setPlatformId(userPlatformMapping.getPlatformId());
        existingUserPlatformMapping.setUsernameOnPlatform(userPlatformMapping.getUsernameOnPlatform());
        return userPlatformMappingRepository.save(existingUserPlatformMapping);
    }

    public UserPlatformMapping getByUserIdAndPlatformId(int userId, int platformId) {
        return userPlatformMappingRepository.findByUserIdAndPlatformId(userId, platformId).orElse(null);
    }

    public List<UserPlatformMapping> getByUserId(int userId) {
        return userPlatformMappingRepository.findAllByUserId(userId);
    }
}
