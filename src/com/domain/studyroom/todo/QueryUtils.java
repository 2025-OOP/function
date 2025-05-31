// Query 문자열을 파싱하는 유틸 클래스 생성
package com.domain.studyroom.todo;

import java.util.HashMap;
import java.util.Map;

public class QueryUtils {
    public static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                map.put(parts[0], parts[1]);
            }
        }
        return map;
    }
}
