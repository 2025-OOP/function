package com.domain.studyroom.timer;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TimerRepository {
    private final String FILE_PATH = Paths.get("src", "com", "domain", "studyroom", "user", "users.json").toString();

    public void saveUserTime(String userId, long elapsedTime) {
        Map<String, Long> userTimes = loadAllUserTimes();
        userTimes.put(userId, elapsedTime);
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(new com.google.gson.Gson().toJson(userTimes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long loadUserTime(String userId) {
        Map<String, Long> userTimes = loadAllUserTimes();
        return userTimes.getOrDefault(userId, 0L);
    }

    private Map<String, Long> loadAllUserTimes() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            return new com.google.gson.Gson().fromJson(reader, HashMap.class);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}
