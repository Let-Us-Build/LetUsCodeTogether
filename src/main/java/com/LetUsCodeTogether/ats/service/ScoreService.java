package com.LetUsCodeTogether.ats.service;

import com.LetUsCodeTogether.ats.entity.DailyActivity;
import com.LetUsCodeTogether.ats.entity.Platform;
import com.LetUsCodeTogether.ats.entity.Score;
import com.LetUsCodeTogether.ats.entity.UserPlatformMapping;
import com.LetUsCodeTogether.ats.repo.ScoreRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoreService {
    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserPlatformMappingService userPlatformMappingService;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private OverallRankService overallRankService;

    @Autowired
    private DailyActivityService dailyActivityService;

    public String addOrUpdateScore(int userId) throws Exception {
        try {
            List<Score> scores = calculateScoreFromPlatforms(userId);
            addDailyActivityForUser(userId, scores);
            for (Score score :
                    scores) {
                Score existingScore = scoreRepository.findByUserIdAndPlatformId(score.getUserId(), score.getPlatformId());
                if (existingScore != null) {
                    existingScore.setNoOfContests(score.getNoOfContests());
                    existingScore.setRatings(score.getRatings());
                    existingScore.setPoints(score.getPoints());
                    existingScore.setNoOfProblemsSolved(score.getNoOfProblemsSolved());
                    existingScore.setCalculatedTotalScore(score.getCalculatedTotalScore());
                    scoreRepository.save(existingScore);
                } else {
                    score.setCreatedDate(null);
                    scoreRepository.save(score);
                }
                calculateAndSetRanksForPlatforms();
                overallRankService.calculateOverallRank();
            }
            return "Scores Added/Updated Successfully";
        } catch (Exception e) {
            return "Failed to update" + e;
        }
    }

    @Async
    private void addDailyActivityForUser(int userId, List<Score> newScores) {
        List<Score> scores;
        if (userId == 0) {
            scores = getAllScores();
        } else {
            scores = getScoreByUserId(userId);
        }
        Map<String, Score> scoreMap = mapScoreToAKey(scores);
        Map<String, Score> newScoreMap = mapScoreToAKey(newScores);
        Map<String, Long> platformStreakMap = calculatePlatformStreak(newScores, scoreMap);
        Map<Long, Long> overallStreakMap = calculateOverallStreak(newScores, scoreMap);
        for (Map.Entry<String, Score> entry : newScoreMap.entrySet()) {
            String key = entry.getKey();
            Score newScore = entry.getValue();
            Score oldScore = scoreMap.getOrDefault(key, null);
            DailyActivity dailyActivity = new DailyActivity();
            dailyActivity.setUserId(newScore.getUserId());
            dailyActivity.setPlatformId(newScore.getPlatformId());
            long problemsSolvedDifference = (oldScore != null)
                    ? newScore.getNoOfProblemsSolved() - oldScore.getNoOfProblemsSolved()
                    : newScore.getNoOfProblemsSolved();
            dailyActivity.setProblemsSolved(problemsSolvedDifference);
            long contestsParticipatedDifference = (oldScore != null)
                    ? newScore.getNoOfContests() - oldScore.getNoOfContests()
                    : newScore.getNoOfContests();
            dailyActivity.setContestsParticipated(contestsParticipatedDifference);
            long ratingsDifference = (oldScore != null)
                    ? (long) (newScore.getRatings() - oldScore.getRatings())
                    : (long) newScore.getNoOfContests();
            dailyActivity.setRatings(contestsParticipatedDifference);
            Double pointsDifference = (oldScore != null)
                    ? newScore.getPoints() - oldScore.getPoints()
                    : newScore.getPoints();
            dailyActivity.setPoints(pointsDifference);
            Double scoreDifference = (oldScore != null)
                    ? newScore.getCalculatedTotalScore() - oldScore.getCalculatedTotalScore()
                    : newScore.getCalculatedTotalScore();
            dailyActivity.setCalculatedTotalScore(scoreDifference);
            if(oldScore!=null) {
                dailyActivity.setPreviousScore(oldScore.getCalculatedTotalScore());
            }
            dailyActivity.setScoreDifference(scoreDifference);
            Long platformStreak = platformStreakMap.getOrDefault(key, 1L);
            dailyActivity.setStreakInDays(platformStreak);
            Long overallStreak = overallStreakMap.getOrDefault(newScore.getUserId(), 1L);
            dailyActivity.setOverallStreakInDays(overallStreak);
            if (userId != 0) {
                dailyActivity.setCreatedBy(newScore.getUserId());
            } else {
                dailyActivity.setCreatedBy(userId);
            }
            dailyActivity.setCreatedDate(null);
            dailyActivityService.addDailyActivity(dailyActivity);
        }
    }

    private Map<String, Score> mapScoreToAKey(List<Score> scores) {
        Map<String, Score> scoreMap = new HashMap<>();
        for (Score score : scores) {
            String key = score.getUserId() + "-" + score.getPlatformId();
            scoreMap.put(key, score);
        }
        return scoreMap;
    }

    @Async
    private Map<String, Long> calculatePlatformStreak(List<Score> newScores, Map<String, Score> oldScoreMap) {
        Map<String, Long> platformStreakMap = new HashMap<>();
        for (Score newScore : newScores) {
            long userId = newScore.getUserId();
            int platformId = newScore.getPlatformId();
            String key = userId + "-" + platformId;
            Score oldScore = oldScoreMap.get(key);
            DailyActivity lastActivity = dailyActivityService.getLatestActivityByUserIdAndPlatformId(userId, platformId);
            Long platformStreak = 1L;
            if (lastActivity != null && oldScore != null && hasActivityChanged(newScore, oldScore)) {
                Calendar lastActivityDate = lastActivity.getCreatedDate();
                Calendar today = Calendar.getInstance();
                today.add(Calendar.DAY_OF_YEAR, -1);
                if (isSameDay(today, lastActivityDate)) {
                    platformStreak = lastActivity.getStreakInDays() + 1;
                }
            }
            platformStreakMap.put(key, platformStreak);
        }
        return platformStreakMap;
    }

    @Async
    private Map<Long, Long> calculateOverallStreak(List<Score> newScores, Map<String, Score> oldScoreMap) {
        Map<Long, Long> overallStreakMap = new HashMap<>();
        Set<Long> userIds = newScores.stream()
                .map(Score::getUserId)
                .collect(Collectors.toSet());
        for (Long userId : userIds) {
            DailyActivity lastOverallActivity = dailyActivityService.getLatestActivity(userId);
            Long overallStreak = 1L;
            boolean activityChanged = newScores.stream()
                    .filter(score -> score.getUserId() == userId)
                    .anyMatch(newScore -> {
                        String key = newScore.getUserId() + "-" + newScore.getPlatformId();
                        Score oldScore = oldScoreMap.get(key);
                        return oldScore != null && hasActivityChanged(newScore, oldScore);
                    });
            if (lastOverallActivity != null && activityChanged) {
                Calendar lastActivityDate = lastOverallActivity.getCreatedDate();
                Calendar today = Calendar.getInstance();
                today.add(Calendar.DAY_OF_YEAR, -1);
                if (isSameDay(today, lastActivityDate)) {
                    overallStreak = lastOverallActivity.getOverallStreakInDays() + 1;
                }
            }
            overallStreakMap.put(userId, overallStreak);
        }
        return overallStreakMap;
    }


    private boolean hasActivityChanged(Score newScore, Score oldScore) {
        return (newScore.getNoOfProblemsSolved() != oldScore.getNoOfProblemsSolved()) ||
                (newScore.getNoOfContests() != oldScore.getNoOfContests()) ||
                (Double.compare(newScore.getPoints(), oldScore.getPoints()) != 0) ||
                (Double.compare(newScore.getRatings(), oldScore.getRatings()) != 0);
    }


    private boolean isSameDay(Calendar day1, Calendar day2) {
        return day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR) &&
                day1.get(Calendar.DAY_OF_YEAR) == day2.get(Calendar.DAY_OF_YEAR);
    }

    private List<Score> calculateScoreFromPlatforms(int userId) {
        try {
            List<Score> scores = new ArrayList<>();
            List<UserPlatformMapping> userPlatformMappings;
            if (userId == 0) {
                userPlatformMappings = userPlatformMappingService.getAllUserPlatformMappings();
            } else {
                userPlatformMappings = userPlatformMappingService.getByUserId(userId);
            }
            List<Platform> platforms = platformService.getAllPlatforms();
            Map<Integer, Platform> platformMap = new HashMap<>();
            for(Platform platform: platforms) {
                platformMap.put(platform.getPlatformId(), platform);
            }
            for (UserPlatformMapping userPlatformMapping :
                    userPlatformMappings) {
                Platform platform = platformMap.getOrDefault(userPlatformMapping.getPlatformId(), null);
                //Platform platform = platformService.getPlatformById(userPlatformMapping.getPlatformId());
                Score score = new Score();
                score.setPlatformId(userPlatformMapping.getPlatformId());
                score.setUserId(userPlatformMapping.getUserId());
                Score scoreFromPlatform = null;
                switch (platform.getPlatformName().toLowerCase()) {
                    case "codechef":
                        scoreFromPlatform = getScoreFromCodechef(userPlatformMapping.getUsernameOnPlatform());
                        break;
                    case "codeforces":
                        scoreFromPlatform = getScoreFromCodeforcesV2(userPlatformMapping.getUsernameOnPlatform());
                        break;
                    case "interviewbit":
                        scoreFromPlatform = getScoreFromInterviewbitV2(userPlatformMapping.getUsernameOnPlatform());
                        break;
                    case "spoj":
                        scoreFromPlatform = getScoreFromSpoj(userPlatformMapping.getUsernameOnPlatform());
                        break;
                    case "hackerrank":
                        scoreFromPlatform = getScoreFromHackerrank(userPlatformMapping.getUsernameOnPlatform());
                        break;
                    case "leetcode":
                        scoreFromPlatform = getScoreFromLeetcode(userPlatformMapping.getUsernameOnPlatform());
                        break;
                    case "default":
                        break;

                }
                score.setNoOfProblemsSolved(scoreFromPlatform.getNoOfProblemsSolved());
                score.setRatings(scoreFromPlatform.getRatings());
                score.setPoints(scoreFromPlatform.getPoints());
                score.setNoOfContests(scoreFromPlatform.getNoOfContests());
                score.setCalculatedTotalScore(scoreFromPlatform.getCalculatedTotalScore());
                scores.add(score);
            }
            return scores;
        } catch (Exception e){
            System.out.println("Exception Occurred\n : "+e.getMessage());
            return null;
        }
    }

    private Score getScoreFromLeetcode(String usernameOnPlatform) {
        Score score = new Score();
        if(usernameOnPlatform.equalsIgnoreCase("")){
            return score;
        }
        try {
            String url = "https://leetcode.com/graphql/";
            String ContestsQuery = "{\"query\":\"\\n    query userContestRankingInfo($username: String!)" +
                    " {\\n  userContestRanking(username: $username) {\\n    attendedContestsCount\\n    rating\\n" +
                    "    globalRanking\\n    totalParticipants\\n    topPercentage\\n    badge {\\n      name\\n  " +
                    "  }\\n  }\\n  userContestRankingHistory(username: $username) {\\n    attended\\n    " +
                    "trendDirection\\n    problemsSolved\\n    totalProblems\\n    finishTimeInSeconds\\n    rating\\n" +
                    "    ranking\\n    contest {\\n      title\\n      startTime\\n    }\\n  }\\n}\\n    " +
                    "\",\"variables\":{\"username\":\"" + usernameOnPlatform + "\"}," +
                    "\"operationName\":\"userContestRankingInfo\"}";

            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "application/json");
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] contestsInput = ContestsQuery.getBytes("utf-8");
                os.write(contestsInput, 0, contestsInput.length);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode detailsNode = objectMapper.readTree(response.toString());
            JsonNode contestsNode = detailsNode.get("data").get("userContestRanking");
            if (!contestsNode.isMissingNode()) {
                score.setNoOfContests(contestsNode.path("attendedContestsCount").asInt());
            }
            if(!contestsNode.isMissingNode()) {
                score.setRatings(contestsNode.path("rating").asDouble());
            }
            connection.getInputStream().close();
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "application/json");
            connection.setDoOutput(true);
            String problemsSolvedQuery = "{\"query\":\"\\n    query userProblemsSolved($username: String!) " +
                    "{\\n  allQuestionsCount {\\n    difficulty\\n    count\\n  }\\n  " +
                    "matchedUser(username: $username) {\\n    problemsSolvedBeatsStats {\\n      difficulty\\n" +
                    "      percentage\\n    }\\n    submitStatsGlobal {\\n      acSubmissionNum {\\n        " +
                    "difficulty\\n        count\\n      }\\n    }\\n  }\\n}\\n    " +
                    "\",\"variables\":{\"username\":\"" + usernameOnPlatform + "\"}," +
                    "\"operationName\":\"userProblemsSolved\"}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = problemsSolvedQuery.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            detailsNode = objectMapper.readTree(response.toString());
            JsonNode acSubmissionNumNode = detailsNode.get("data").get("matchedUser").get("submitStatsGlobal")
                    .get("acSubmissionNum");

            if (acSubmissionNumNode != null && acSubmissionNumNode.isArray()) {
                JsonNode countNode = acSubmissionNumNode.get(0).get("count");

                if (countNode != null) {
                    score.setNoOfProblemsSolved(Integer.parseInt(String.valueOf(countNode)));
                }
            }
            if(score.getNoOfContests()>=3 && score.getRatings() > 1300){
                score.setCalculatedTotalScore(
                        ((score.getNoOfProblemsSolved() * 50) +
                                (Math.pow(score.getRatings() - 1300, 2) / 30))
                );
            } else {
                score.setCalculatedTotalScore(score.getNoOfProblemsSolved() * 50);
            }
            connection.getInputStream().close();
        } catch (Exception e) {
            System.out.println("LeetCode Error: Internal Server Error, Please contact Administrator. "+e);
        }
        return score;
    }

    private Score getScoreFromHackerrank(String usernameOnPlatform) throws Exception {
        Score score = new Score();
        if(usernameOnPlatform.equalsIgnoreCase("")){
            return score;
        }
        URL url = new URL("https://www.hackerrank.com/rest/hackers/" + usernameOnPlatform + "/scores_elo");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode detailsNode = objectMapper.readTree(response.toString());
            if (detailsNode != null && detailsNode.isArray()) {
                JsonNode practiceScoreNode1 = detailsNode.get(1);
                JsonNode practiceScoreNode9 = detailsNode.get(9);

                if (practiceScoreNode1 != null && practiceScoreNode1.get("practice") != null) {
                    score.setRatings(Double.parseDouble(String.valueOf(practiceScoreNode1.get("practice").get("score"))));
                }

                if (practiceScoreNode9 != null && practiceScoreNode9.get("practice") != null) {
                    score.setRatings(score.getRatings() + Double.parseDouble(String.valueOf(practiceScoreNode9
                            .get("practice").get("score"))));
                }
            }
            score.setCalculatedTotalScore(score.getRatings());
        } catch (Exception e) {
            System.out.println("Hackkerrank Error: Internal Server Error"+ e);
        }
        return score;
    }

    private Score getScoreFromCodeforcesV1(String usernameOnPlatform){
        System.setProperty("http.proxyHost", "proxy.example.com");
        System.setProperty("http.proxyPort", "8080");
        Score score = new Score();
        if(usernameOnPlatform.equalsIgnoreCase("")){
            return score;
        }
        String url = "https://codeforces.com/profile/" + usernameOnPlatform;
        String contestsUrl = "https://codeforces.com/contests/with/" + usernameOnPlatform;
        try {
            //Document document = Jsoup.connect(url).get();
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                    .get();
            Element ratingElement = document.selectFirst("div.info").selectFirst("ul")
                    .selectFirst("li").selectFirst("span");
            Element noOfProblemSolvedElement =
                    document.selectFirst("._UserActivityFrame_counterValue");
            //Document contestDocument = Jsoup.connect(contestsUrl).get();
            Document contestDocument = Jsoup.connect(contestsUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                    .get();
            Element noOfContestsParticipatedElement = contestDocument.selectFirst("table.user-contests-table")
                    .selectFirst("tbody").selectFirst("tr").selectFirst("td");
            if (ratingElement != null) {
                score.setRatings(Integer.parseInt(ratingElement.text()));
                score.setNoOfContests(Integer.parseInt(noOfContestsParticipatedElement.text()));
                String extractedText = noOfProblemSolvedElement.text();
                score.setNoOfProblemsSolved(Integer.parseInt(extractedText.substring(0,
                        extractedText.indexOf(" "))));
                if(score.getNoOfContests()>=3 && score.getRatings() > 1200){
                    score.setCalculatedTotalScore(
                            (int) ((score.getNoOfProblemsSolved() * 10) +
                                    (Math.pow(score.getRatings() - 1200, 2) / 30))
                    );
                } else {
                    score.setCalculatedTotalScore(score.getNoOfProblemsSolved() * 10);
                }
            } else {
                System.out.println("CodeForces Rating Not found");
            }
        } catch (Exception e) {
            System.out.println("CodeForces Error: Internal Server Error, Please Contact Administrator"+ e);
        }
        return score;
    }

    private Score getScoreFromSpoj(String usernameOnPlatform) {
        Score score = new Score();
        if(usernameOnPlatform.equalsIgnoreCase("")){
            return score;
        }
        String url = "https://www.spoj.com/users/" + usernameOnPlatform;
        try {
            Document document = Jsoup.connect(url).get();
            Element problemsSolvedElement =
                    document.selectFirst(".profile-info-data-stats").selectFirst("dd");
            Element pointsElement = document.selectFirst("div#user-profile-left").select("p").get(2);
            if (problemsSolvedElement != null) {
                score.setNoOfProblemsSolved(Integer.parseInt(problemsSolvedElement.text()));
                String extractedText = pointsElement.text();
                Double points = Double.parseDouble(extractedText.substring(extractedText.indexOf("(")+1,
                        extractedText.indexOf(" p")));
                score.setPoints(points);
                score.setCalculatedTotalScore((int) ((score.getNoOfProblemsSolved() * 20) + (score.getPoints()*500)));
            } else {
                System.out.println("Spoj Score Not found");
            }
        } catch (Exception e) {
            System.out.println("Spoj Error: Internal Server Error, Please Contact Administrator"+ e);
        }
        return score;
    }

    private Score getScoreFromInterviewbitV1(String usernameOnPlatform) {
        Score score = new Score();
        if(usernameOnPlatform.equalsIgnoreCase("")){
            return score;
        }
        String url = "https://www.interviewbit.com/profile/" + usernameOnPlatform;
        try {
            Document document = Jsoup.connect(url).get();
            // Score considered as rating
            Element ratingElement = document.select(".user-stats .stat.pull-left .txt").get(1);
            //Element ratingElement = document.selectFirst(".profile-daily-goal__goal-details");//.get(1);
            if (ratingElement != null) {
                score.setRatings(Integer.parseInt(ratingElement.text()));
                score.setCalculatedTotalScore((int) (score.getRatings() / 3));
            } else {
                System.out.println("Interviewbit Score Not found");
            }
        } catch (Exception e) {
            System.out.println("Interviewbit Error: Internal Server Error, Please Contact Administrator"+ e);
        }
        return score;
    }

    private Score getScoreFromInterviewbitV2(String usernameOnPlatform) throws Exception {
        Score score = new Score();
        if(usernameOnPlatform.equalsIgnoreCase("")){
            return score;
        }
        URL url = new URL("https://www.interviewbit.com/v2/profile/" + usernameOnPlatform + "/streak/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode detailsNode = objectMapper.readTree(response.toString());
            JsonNode scoreNode = detailsNode.get("score");
            if (scoreNode != null) {
                score.setCalculatedTotalScore(Double.parseDouble(scoreNode.toString())/3);
            } else{
                System.out.println("Score not found for InterviewBit(V2)");
            }
        } catch (Exception e) {
            System.out.println("Interviewbit Error: Internal Server Error"+ e);
        }
        return score;
    }

    private Score getScoreFromCodechef(String usernameOnPlatform) {
        Score score = new Score();
        if(usernameOnPlatform.equalsIgnoreCase("")){
            return score;
        }
        String url = "https://www.codechef.com/users/" + usernameOnPlatform;
        try {
            Document document = Jsoup.connect(url).get();
            Element ratingElement = document.selectFirst("div.rating-number");
            Element noOfPracticeProblemSolvedElement =
                    document.selectFirst("section.problems-solved h3:contains(Total Problems Solved)");
            Element noOfContestProblemSolvedElement =
                    document.selectFirst("section.problems-solved h3:contains(Contests)");
            Element noOfContestsParticipatedElement = document.selectFirst("div.contest-participated-count");
            if (ratingElement != null) {
                score.setRatings(Integer.parseInt(ratingElement.text()));
                score.setNoOfContests(Integer.parseInt(noOfContestsParticipatedElement.text().substring(30)));
                String extractedText = noOfPracticeProblemSolvedElement.text();
                Integer noOfProblemsSolved = Integer.parseInt(extractedText.substring(23));
                score.setNoOfProblemsSolved(noOfProblemsSolved);
                if(score.getNoOfContests()>=3 && score.getRatings() > 1300){
                    score.setCalculatedTotalScore(
                            ((score.getNoOfProblemsSolved() * 10) +
                                                                (Math.pow(score.getRatings() - 1300, 2) / 30))
                    );
                } else {
                    score.setCalculatedTotalScore(score.getNoOfProblemsSolved() * 10);
                }
            } else {
                System.out.println("Codechef Rating Not found");
            }
        } catch (Exception e) {
            System.out.println("Codechef Error: Internal Server Error, Please Contact Administrator "+ e);
        }
        return score;
    }

    public Score getByUserIdAndPlatformId(int userId, int platformId) {
        return scoreRepository.findByUserIdAndPlatformId(userId, platformId);
    }

    private void calculateAndSetRanksForPlatforms() {
        List<Platform> platforms = platformService.getAllPlatforms();
        for(Platform platform: platforms) {
            Sort sort = Sort.by(Sort.Direction.DESC, "calculated_total_score");
            List<Score> scores = scoreRepository.findAllByPlatformId(platform.getPlatformId());
            Collections.sort(scores, new Comparator<Score>() {
                @Override
                public int compare(Score score1, Score score2) {
                    return Double.compare(score2.getCalculatedTotalScore(), score1.getCalculatedTotalScore());
                }
            });
            int currentRank = 1, currentPlatformRank = 1;
            double currentScore = Double.MAX_VALUE;
            for(Score score: scores){
                if (score.getCalculatedTotalScore() < currentScore) {
                    currentPlatformRank = currentRank;
                    currentScore = score.getCalculatedTotalScore();
                }
                score.setLuctRank(currentPlatformRank);
                scoreRepository.save(score);
                currentRank++;
            }
        }
    }

    public Score getScoreFromCodeforcesV2(String usernameOnPlatform) {
        Score score = new Score();

        if (usernameOnPlatform.equalsIgnoreCase("")) {
            return score;
        }

        String userInfoApiUrl = "https://codeforces.com/api/user.info?handles=" + usernameOnPlatform;
        String contestHistoryApiUrl = "https://codeforces.com/api/user.rating?handle=" + usernameOnPlatform;
        String userStatusApiUrl = "https://codeforces.com/api/user.status?handle=" + usernameOnPlatform;

        try {
            String userInfoResponse = fetchCodeforcesJSONResponse(userInfoApiUrl);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode userInfoRootNode = mapper.readTree(userInfoResponse);
            JsonNode userData = userInfoRootNode.path("result").get(0);

            int rating = userData.path("rating").asInt(0);
            score.setRatings(rating);

            String contestHistoryResponse = fetchCodeforcesJSONResponse(contestHistoryApiUrl);
            JsonNode contestHistoryRootNode = mapper.readTree(contestHistoryResponse);
            int noOfContestsParticipated = contestHistoryRootNode.path("result").size(); // Count of contests
            score.setNoOfContests(noOfContestsParticipated);

            String userStatusResponse = fetchCodeforcesJSONResponse(userStatusApiUrl);
            JsonNode userStatusRootNode = mapper.readTree(userStatusResponse);
            Set<String> uniqueProblemsSolved = new HashSet<>();

            for (JsonNode submission : userStatusRootNode.path("result")) {
                if (submission.path("verdict").asText().equals("OK")) {
                    String contestId = submission.path("contestId").asText();
                    String problemIndex = submission.path("problem").path("index").asText();
                    String problemId = contestId + "-" + problemIndex;
                    uniqueProblemsSolved.add(problemId);
                }
            }

            score.setNoOfProblemsSolved(uniqueProblemsSolved.size());

            if (score.getNoOfContests() >= 3 && score.getRatings() > 1200) {
                score.setCalculatedTotalScore(
                        (int) ((score.getNoOfProblemsSolved() * 10) + (Math.pow(score.getRatings() - 1200, 2) / 30))
                );
            } else {
                score.setCalculatedTotalScore(score.getNoOfProblemsSolved() * 10);
            }
        } catch (Exception e) {
            System.out.println("Codeforces Error: Internal Server Error, Please Contact Administrator"+ e);
        }
        return score;
    }

    private String fetchCodeforcesJSONResponse(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            System.out.println("Error: Unable to connect to the API, Response Code: " + responseCode);
        }

        Scanner sc = new Scanner(url.openStream());
        StringBuilder jsonResponse = new StringBuilder();
        while (sc.hasNext()) {
            jsonResponse.append(sc.nextLine());
        }
        sc.close();

        return jsonResponse.toString();
    }

    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }

    public List<Score> getScoreByUserId(int userId) {
        return scoreRepository.findAllByUserId(userId);
    }

}
