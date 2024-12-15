package com.LetUsCodeTogether.ats.service;

import com.LetUsCodeTogether.ats.entity.Contest;
import com.LetUsCodeTogether.ats.entity.Platform;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContestService {

    @Autowired
    private PlatformService platformService;

    public List<Contest> getContests(String platform) {
        try {
            switch (platform.toLowerCase()) {
                case "codechef":
                    return codechefContests();
                case "codeforces":
                    return codeforcesContests();
                case "leetcode":
                    return leetcodeContests();
                default:
                    throw new RuntimeException("Contests not available for the platform");
            }
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    private Map<String, Platform> getPlatformsMap(){
        List<Platform> platforms = platformService.getAllPlatforms();
        Map<String, Platform> platformMap = new HashMap<>();
        for(Platform platform: platforms) {
            platformMap.put(platform.getPlatformName().toLowerCase(), platform);
        }
        return platformMap;
    }

    private List<Contest> leetcodeContests() {
        int platformId = getPlatformsMap().get("leetcode").getPlatformId();
        String urlString = "https://leetcode.com/graphql?query={%20allContests%20{%20title%20titleSlug%20startTime%20duration%20__typename%20}%20}";
        List<Contest> contests = new ArrayList<>();

        try {
            // Open connection
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check for successful response
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed to fetch contests from LeetCode: HTTP code " + responseCode);
            }

            // Read response
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.toString());
            JsonNode contestsNode = root.path("data").path("allContests");

            // Map each contest
            if (contestsNode.isArray()) {
                for (JsonNode contestNode : contestsNode) {
                    Contest contest = new Contest();

                    contest.setContestName(contestNode.path("title").asText());
                    contest.setPlatformId(platformId); // Assuming platform ID 1 represents LeetCode
                    contest.setType("LeetCode Contest"); // Set type as needed
                    contest.setPlatformContestId(0); // No platformContestId in response
                    contest.setFrozen("false"); // LeetCode contests are not frozen
                    contest.setDuration(contestNode.path("duration").asLong());
                    contest.setStartTime(convertUnixTimestampToDate(contestNode.path("startTime").asLong()));
                    contest.setEndTime(convertUnixTimestampToDate(contestNode.path("startTime").asLong() + contest.getDuration()));
                    contest.setContestCode(contestNode.path("titleSlug").asText());
                    setContestPhase(contest, contestNode);
                    contests.add(contest);
                }
            }

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return contests;
    }

    private void setContestPhase(Contest contest, JsonNode contestNode) {
        long currentTime = Instant.now().getEpochSecond(); // Current time in seconds

        if (currentTime < contestNode.path("startTime").asLong()) {
            contest.setPhase("UPCOMING");
        } else if (currentTime > contestNode.path("startTime").asLong() + contestNode.path("duration").asLong()) {
            contest.setPhase("FINISHED");
        } else {
            contest.setPhase("RUNNING"); // Optional: Handle cases where the contest is currently active
        }
    }

    private List<Contest> codeforcesContests() {
        int platformId = getPlatformsMap().get("codeforces").getPlatformId();
        String urlString = "https://codeforces.com/api/contest.list";
        List<Contest> contests = new ArrayList<>();

        try {
            // Open connection
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check for successful response
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed to fetch contests from Codeforces: HTTP code " + responseCode);
            }

            // Read response
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.toString());
            String status = root.path("status").asText();

            if (!"OK".equalsIgnoreCase(status)) {
                throw new RuntimeException("Failed to fetch contests from Codeforces: API returned status " + status);
            }

            JsonNode resultNode = root.path("result");

            if (resultNode.isArray()) {
                for (JsonNode contestNode : resultNode) {
                    Contest contest = new Contest();

                    contest.setContestName(contestNode.path("name").asText());
                    contest.setPlatformId(platformId); // Assuming platform ID 2 represents Codeforces
                    contest.setType(contestNode.path("type").asText());
                    contest.setPlatformContestId(contestNode.path("id").asLong());
                    contest.setPhase(contestNode.path("phase").asText().equalsIgnoreCase("BEFORE") ?
                            "UPCOMING" : contestNode.path("phase").asText());
                    contest.setFrozen(contestNode.path("frozen").asText());
                    contest.setDuration(contestNode.path("durationSeconds").asLong());
                    contest.setStartTime(convertUnixTimestampToDate(contestNode.path("startTimeSeconds").asLong()));
                    contest.setEndTime(convertUnixTimestampToDate(contestNode.path("startTimeSeconds").asLong() + contest.getDuration()));
                    contest.setContestCode(contestNode.path("id").asText());

                    contests.add(contest);
                }
            }

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return contests;
    }

    private List<Contest> codechefContests() {
        int platformId = getPlatformsMap().get("codechef").getPlatformId();
        String urlString = "https://www.codechef.com/api/list/contests/all?sort_by=START&sorting_order=asc&offset=0&mode=all";
        List<Contest> contests = new ArrayList<>();

        try {
            // Open connection
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check for successful response
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed to fetch contests from CodeChef: HTTP code " + responseCode);
            }

            // Read response
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            // Parse JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.toString());

            // Map each contest
            JsonNode presentContests = root.path("present_contests");
            JsonNode futureContests = root.path("future_contests");
            JsonNode pastContests = root.path("past_contests");

            // Helper function to map contests
            mapCodechefContests(presentContests, contests, platformId, "RUNNING");
            mapCodechefContests(futureContests, contests, platformId, "UPCOMING");
            mapCodechefContests(pastContests, contests, platformId, "FINISHED");

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            // Log the error if needed
        }

        return contests;
    }

    private void mapCodechefContests(JsonNode contestArray, List<Contest> contests, long platformId, String status) {
        if (contestArray.isArray()) {
            for (JsonNode contestNode : contestArray) {
                Contest contest = new Contest();

                contest.setContestName(contestNode.path("contest_name").asText());
                contest.setPlatformId(platformId);
                contest.setType("CodeChef Contest"); // Default type, update if necessary
                contest.setPlatformContestId(0); // No specific ID in API
                contest.setPhase(status); // Default phase, update if necessary
                contest.setFrozen("false"); // Assuming contests are not frozen
                contest.setDuration(contestNode.path("contest_duration").asLong() * 60L);
                contest.setStartTime(contestNode.path("contest_start_date").asText());
                contest.setEndTime(contestNode.path("contest_end_date").asText());
                contest.setContestCode(contestNode.path("contest_code").asText()); // HashCode for uniqueness

                contests.add(contest);
            }
        }
    }

    private String convertUnixTimestampToDate(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()).toString();
    }
}
