package com.LetUsCodeTogether.ats.codeChef;

import com.LetUsCodeTogether.ats.entity.Score;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CodeChef {
    public void fetchScoresFromCodechef() throws IOException {
        String username = "saigirishpabba";
        String url = "https://www.codechef.com/users/" + username;

        Document document = Jsoup.connect(url).get();
        Element ratingElement = document.selectFirst("div.rating-number");
        Element noOfPracticeProblemSolved = document.selectFirst("section.problems-solved h3:contains(Practice Problems)");
        Element noOfContestProblemSolved = document.selectFirst("section.problems-solved h3:contains(Contests)");
        Element noOfContestsParticipated = document.selectFirst("div.contest-participated-count");
        if (ratingElement != null) {
            String rating = ratingElement.text();
            String statement = noOfPracticeProblemSolved.text();
            System.out.println("User: " + username + ", CodeChef Rating: " + rating + " "+ statement.substring(statement.indexOf("(")+1, statement.indexOf(")"))
                    + " "+noOfContestProblemSolved.text()+"\n " +noOfContestsParticipated.text());
        } else {
            System.out.println("User: " + username + ", CodeChef Rating not found.");
        }

        username = "neal_wu";
        try {
            url = "https://leetcode.com/graphql/";
            String ContestsQuery = "{\"query\":\"\\n    query userContestRankingInfo($username: String!)" +
                    " {\\n  userContestRanking(username: $username) {\\n    attendedContestsCount\\n    rating\\n    globalRanking\\n    totalParticipants\\n    topPercentage\\n    badge {\\n      name\\n    }\\n  }\\n  userContestRankingHistory(username: $username) {\\n    attended\\n    trendDirection\\n    problemsSolved\\n    totalProblems\\n    finishTimeInSeconds\\n    rating\\n    ranking\\n    contest {\\n      title\\n      startTime\\n    }\\n  }\\n}\\n    \",\"variables\":{\"username\":\"" + username + "\"},\"operationName\":\"userContestRankingInfo\"}";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("content-type", "application/json");
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = ContestsQuery.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Response: " + response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }


        username = "sharan-sai";
        String urlInterviewbit = "https://www.interviewbit.com/profile/" + username;
        document = Jsoup.connect(urlInterviewbit).get();
        // Score considered as rating
        ratingElement = document.select(".user-stats .stat.pull-left .txt").get(1);
        System.out.println(ratingElement);
        if (ratingElement != null) {
            System.out.println(ratingElement.text());
        } else {
            System.out.println("User: " + username + ", Interviewbit Score not found.");
        }




        username = "saigirishpabba";
        String urlSpoj = "https://www.spoj.com/users/" + username;
        document = Jsoup.connect(urlSpoj).get();
        ratingElement = document.selectFirst(".profile-info-data-stats").selectFirst("dd");
        Element pointsElement = document.selectFirst("div#user-profile-left").select("p").get(2);
        String extractedText = pointsElement.text();
        System.out.println(Double.parseDouble(extractedText.substring(extractedText.indexOf("(")+1,
                extractedText.indexOf(" p"))));
        if (ratingElement != null) {
            System.out.println(ratingElement.text());
        } else {
            System.out.println("User: " + username + ", Spoj Score not found.");
        }

        url = "https://codeforces.com/profile/saigirishpabbathi";
        String contestsUrl = "https://codeforces.com/contests/with/saigirishpabbathi";
        Score score = new Score();
        document = Jsoup.connect(url).get();
        ratingElement = document.selectFirst("div.info").selectFirst("ul")
                .selectFirst("li").selectFirst("span");
        Element noOfProblemSolvedElement =
                document.selectFirst("._UserActivityFrame_counterValue");
        Document contestDocument = Jsoup.connect(contestsUrl).get();
        Element noOfContestsParticipatedElement = contestDocument.selectFirst("table.user-contests-table")
                .selectFirst("tbody").selectFirst("tr").selectFirst("td");
        if (ratingElement != null) {
            System.out.println(Integer.parseInt(ratingElement.text()));
            System.out.println(Integer.parseInt(noOfContestsParticipatedElement.text()));
            extractedText = noOfProblemSolvedElement.text();
            System.out.println(Integer.parseInt(extractedText.substring(0,
                    extractedText.indexOf(" "))));
        } else {
            System.out.println("Codeforces Rating Not found");
        }


        username = "saigirishpabbat1";
        URL url1 = new URL("https://www.hackerrank.com/rest/hackers/"+username+"/scores_elo");
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode detailsNode = objectMapper.readTree(response.toString());

            System.out.println("Track_id" + detailsNode.get(1).get("track_id"));
        }
    }
}
