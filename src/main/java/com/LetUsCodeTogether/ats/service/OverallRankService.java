package com.LetUsCodeTogether.ats.service;

import com.LetUsCodeTogether.ats.entity.OverallRank;
import com.LetUsCodeTogether.ats.entity.Platform;
import com.LetUsCodeTogether.ats.entity.Score;
import com.LetUsCodeTogether.ats.entity.User;
import com.LetUsCodeTogether.ats.repo.OverallRankRepository;
import com.LetUsCodeTogether.ats.repo.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OverallRankService {
    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private OverallRankRepository overallRankRepository;

    @Async
    public void calculateOverallRank() throws Exception {
        try {
            Sort sort = Sort.by(Sort.Direction.ASC, "userId");
            List<Score> scores = scoreRepository.findAll(sort);
            List<OverallRank> overallRanks = new ArrayList<>();
            long currentUserId = -1;
            double totalScore = 0;

            for (Score score : scores) {
                if (score.getUserId() != currentUserId) {
                    if (currentUserId != -1) {
                        OverallRank overallRank = new OverallRank();
                        overallRank.setUserId(currentUserId);
                        overallRank.setTotalScore(totalScore);
                        overallRanks.add(overallRank);
                        totalScore = 0;
                    }
                    currentUserId = score.getUserId();
                }
                totalScore += score.getCalculatedTotalScore();
            }

            if (currentUserId != -1) {
                OverallRank overallRank = new OverallRank();
                overallRank.setUserId(currentUserId);
                overallRank.setTotalScore(totalScore);
                overallRanks.add(overallRank);
            }

            Collections.sort(overallRanks, (a, b) -> Double.compare(b.getTotalScore(), a.getTotalScore()));
            int rank = 1;

            for (OverallRank overallRank : overallRanks) {
                overallRank.setRank(rank++);
                Optional<OverallRank> existingRankOptional = overallRankRepository.findByUserId(overallRank.getUserId());
                if (existingRankOptional.isPresent()) {
                    OverallRank existingRank = existingRankOptional.get();
                    existingRank.setTotalScore(overallRank.getTotalScore());
                    existingRank.setRank(overallRank.getRank());
                    overallRankRepository.save(existingRank);
                } else {
                    overallRankRepository.save(overallRank);
                }
            }
        } catch (Exception e) {
            throw new Exception("Could not able to calculate rank\n", e);
        }
    }

    public OverallRank getOverallRankByUserId(int userId){
        try{
            Optional<OverallRank> overallRank = overallRankRepository.findByUserId(userId);
            if(overallRank.isPresent()){
                return overallRank.get();
            }
        } catch (Exception e) {
            System.out.println("Failed to fetch overall rank.\n" + e);
        }
        return null;
    }

    public List<OverallRank> getAllOverAllRanks(){
        try{
            List<OverallRank> overallRanks = overallRankRepository.findAll();
            return overallRanks;
        } catch(Exception e) {
            System.out.println("Error in fetching overall ranks\n"+ e);
        }
        return null;
    }

    public List<Map<String, Object>> getOverallRanksAndScoreForLeaderboard() {
        List<Map<String, Object>> responseList = new ArrayList<>();
        List<Platform> platformList = platformService.getAllPlatforms();
        Map<Integer, Platform> platformsMap = new HashMap<>();
        for (Platform platform: platformList) {
            platformsMap.put(platform.getPlatformId(), platform);
        }
        List<User> userList = userService.getAllUsers();
        List<Score> scores = scoreRepository.findAll();
        List<OverallRank> overallRanks = overallRankRepository.findAll();
        Map<Long, OverallRank> overallRankMap = overallRanks.stream()
                .collect(Collectors.toMap(OverallRank::getUserId, Function.identity()));
        for(User user: userList) {
            Map<String, Object> userData = new HashMap<>();
            if(user.getUserId()==0){
                continue;
            }
            OverallRank overallRank = overallRankMap.getOrDefault(user.getUserId(), null);
            if (overallRank != null) {
                userData.put("rank", overallRank.getRank());
                userData.put("overallScore", overallRank.getTotalScore());
            }
            userData.put("name", user.getFirstName() + " " + user.getLastName());
            userData.put("luctUsername", user.getUsername());
            for (Score score : scores.stream().filter(s -> (s.getUserId() == user.getUserId())).toList()) {
                Platform platform = platformsMap.getOrDefault(score.getPlatformId(), null);
                if (platform == null) {
                    continue;
                }
                String platformName = platform.getPlatformName().toLowerCase();
                userData.put(platformName + "LuctRank", score.getLuctRank());
                userData.put(platformName + "ProblemsSolved", score.getNoOfProblemsSolved());
                userData.put(platformName + "Rating", score.getRatings());
                userData.put(platformName + "ContestsParticipated", score.getNoOfContests());
                userData.put(platformName + "TotalScore", score.getCalculatedTotalScore());
            }
            responseList.add(userData);
        }
        responseList.sort((map1, map2) -> {
            Double score1 = map1.get("overallScore") != null ? (Double) map1.get("overallScore") : 0.0;
            Double score2 = map2.get("overallScore") != null ? (Double) map2.get("overallScore") : 0.0;
            return score2.compareTo(score1);
        });
        return responseList;
    }
}
