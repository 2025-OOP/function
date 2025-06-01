package com.domain.studyroom.jitsi;


import com.google.gson.Gson;

public class JsonUtils {
    private static final Gson gson = new Gson();

    // Java 객체 → JSON 문자열
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    // JSON 문자열 → Java 객체
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}