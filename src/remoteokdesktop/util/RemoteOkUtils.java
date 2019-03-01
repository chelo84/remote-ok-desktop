package remoteokdesktop.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.sun.deploy.util.StringUtils;
import org.json.JSONArray;
import remoteokdesktop.model.RemoteOkJob;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class RemoteOkUtils {

    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final String FAVORITES_PATH = "favorites.txt";

    public static List<RemoteOkJob> getJobs() {
        HttpResponse<JsonNode> remoteOkResponse = null;
        List<RemoteOkJob> jobs = null;
        try {
            remoteOkResponse = Unirest.get("https://remoteok.io/api").asJson();
            JSONArray arr = remoteOkResponse.getBody().getArray();
            arr.remove(0);
            ObjectMapper mapper = new ObjectMapper();
            jobs = jsonArrayToJobs(arr);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return jobs;
    }

    public static List<RemoteOkJob> getJobs(String filter) {
        List<RemoteOkJob> jobs = getJobs();

        if(Objects.nonNull(filter) && !filter.isEmpty()) {
            jobs = jobs.stream()
                    .filter(job -> job.getCompany().contains(filter) || job.getSlug().contains(filter))
                    .collect(Collectors.toList());
        }

        return jobs;
    }

    public static void likeJob(RemoteOkJob job) {
        try {

            List<RemoteOkJob> likedJobs = getLikedJobs();
            FileWriter fileWriter = new FileWriter(FAVORITES_PATH);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            likedJobs.add(job);
            printWriter.println(jobArrayToJson(likedJobs));
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dislikeJob(RemoteOkJob job) {
        try {
            List<RemoteOkJob> likedJobs = getLikedJobs();
            FileWriter fileWriter = new FileWriter(FAVORITES_PATH);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            likedJobs.removeAll(likedJobs.stream().filter(j -> j.getId().equals(job.getId())).collect(Collectors.toList()));
            printWriter.println(jobArrayToJson(likedJobs));
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<RemoteOkJob> getLikedJobs() throws IOException {
        String result = Files.readAllLines(Paths.get(FAVORITES_PATH)).stream().collect(Collectors.joining("\n"));

        return jsonArrayToJobs(result);
    }

    private static List<RemoteOkJob> jsonArrayToJobs(String arr) throws IOException {
        if(arr.isEmpty() || isNull(arr)) {
            return new ArrayList<>();
        }

        return mapper.readValue(arr, mapper.getTypeFactory().constructCollectionType(List.class, RemoteOkJob.class));
    }

    private static String jobArrayToJson(List<RemoteOkJob> jobs) throws JsonProcessingException {
        return mapper.writeValueAsString(jobs);
    }

    public static String jobToJson(RemoteOkJob job) throws JsonProcessingException {
        return mapper.writeValueAsString(job);
    }

    public static RemoteOkJob jsonToJob(JsonNode jobJson) throws IOException {
        return mapper.readValue(jobJson.toString(), RemoteOkJob.class);
    }

    public static List<RemoteOkJob> jsonArrayToJobs(JSONArray arr) throws IOException {
        return mapper.readValue(arr.toString(), mapper.getTypeFactory().constructCollectionType(List.class, RemoteOkJob.class));
    }

    public static Boolean isJobLiked(RemoteOkJob job) {
        try {
            return getLikedJobs().stream().anyMatch((j) -> j.getId().equals(job.getId()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public static Boolean isJobLiked(List<RemoteOkJob> likedJobs, RemoteOkJob job) {
        return likedJobs.stream().anyMatch((j) -> j.getId().equals(job.getId()));
    }
}
