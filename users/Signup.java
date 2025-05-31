package com.domain.studyroom.users;// 회원가입 - 콘솔에서 사용자 입력받고 DB(MySQL)에 직접 저장

import java.sql.*;
import java.util.Scanner;

public class Signup {
    public static void main(String[] args) {
        // 1. MySQL DB 연결 정보 설정
        String url = "jdbc:mysql://localhost:3306/java_studyroom_project?serverTimezone=UTC&characterEncoding=UTF-8";
        String dbUser = "";  // DB 사용자
        String dbPassword = "";  // DB 비밀번호

        Scanner scanner = new Scanner(System.in);

        System.out.println("아이디(username): ");
        String username = scanner.nextLine();

        System.out.print("비밀번호 (password): ");
        String password = scanner.nextLine();

        try {
            // 2. MySQL 드라이버 로딩
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 3. DB 연결
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);

            // 4. INSERT SQL 쿼리 준비
            String sql = "INSERT INTO user (username, password) VALUES (?, ?)";

            // 5. PreparedStatement 객체 생성
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // 6. 사용자 입력값을 PreparedStatement에 설정
            pstmt.setString(1, username);   // username
            pstmt.setString(2, password);  // password


            // 7. SQL 쿼리 실행 (INSERT 쿼리 실행)
            int result = pstmt.executeUpdate();  // 실행 결과가 0보다 크면 성공

            if (result > 0) {
                System.out.println("회원가입 성공!");  // 성공 메시지 출력
            } else {
                System.out.println("회원가입 실패...");
            }

            // 8. 자원 정리
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();  // 예외가 발생하면 에러 메시지 출력
        }
    }
}
