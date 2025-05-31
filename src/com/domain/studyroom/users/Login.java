package com.domain.studyroom.users;

import com.domain.studyroom.db.DB;

import java.sql.*;
import java.util.Scanner;

public class Login {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. 사용자 입력
        System.out.print("아이디 (username): ");
        String username = scanner.nextLine();

        System.out.print("비밀번호 (password): ");
        String password = scanner.nextLine();

        try (Connection conn = DB.getConnection()) {
            // 2. SQL 쿼리 준비 (username과 password 일치 확인)
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // 3. 쿼리 실행
            ResultSet rs = pstmt.executeQuery();

            // 4. 결과 확인
            if (rs.next()) {
                String nickname = rs.getString("nickname");
                System.out.println("로그인 성공! 환영합니다, " + (nickname != null ? nickname : username) + "님 :)");
            } else {
                System.out.println("로그인 실패... 아이디 또는 비밀번호를 확인하세요.");
            }

            rs.close();
            pstmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();
    }
}

