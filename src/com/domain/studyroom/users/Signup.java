package com.domain.studyroom.users;

import com.domain.studyroom.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Signup {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("아이디(username): ");
        String username = scanner.nextLine();

        System.out.print("비밀번호 (password): ");
        String password = scanner.nextLine();

        try (Connection conn = DB.getConnection()) {
            // SQL 준비
            String sql = "INSERT INTO user (username, password) VALUES (?, ?)";

            // PreparedStatement 준비 및 값 바인딩
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // 쿼리 실행
            int result = pstmt.executeUpdate();

            if (result > 0) {
                System.out.println("회원가입 성공!");
            } else {
                System.out.println("회원가입 실패...");
            }

            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();
    }
}
