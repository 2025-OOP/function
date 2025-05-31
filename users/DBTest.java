package com.domain.studyroom.users; // MySQL 연결 확인 및 SELECT * FROM user 실행해서 DB 연결 상태 확인

import java.sql.*;

public class DBTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/java_studyroom_project"; // DB 주소
        String user = ""; // 사용자명
        String password = ""; // 비밀번호

        try {
            // 1. 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. 연결
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("DB 연결 성공!");

            // 3. 쿼리 실행
            String sql = "SELECT * FROM user";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // 4. 결과 출력
            while (rs.next()) {
                System.out.println("사용자 ID: " + rs.getString("username"));
            }

            // 5. 연결 종료
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
