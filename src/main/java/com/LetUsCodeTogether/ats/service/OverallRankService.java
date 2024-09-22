package com.LetUsCodeTogether.ats.service;

import com.LetUsCodeTogether.ats.entity.OverallRank;
import com.LetUsCodeTogether.ats.entity.Score;
import com.LetUsCodeTogether.ats.repo.OverallRankRepository;
import com.LetUsCodeTogether.ats.repo.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OverallRankService {
    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private OverallRankRepository overallRankRepository;

    @Async
    public void calculateOverallRank() throws Exception {
        try {
            Sort sort = Sort.by(Sort.Direction.ASC, "userId");
            List<Score> scores = scoreRepository.findAll(sort);
            List<OverallRank> overallRanks = new ArrayList<>();
            int currentUserId = -1;
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
}
