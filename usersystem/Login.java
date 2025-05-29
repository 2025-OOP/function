package usersystem;

import java.sql.*;
import java.util.Scanner;

public class Login {
    public static void main(String[] args) {
        // 1. DB 연결 정보
        String url = "jdbc:mysql://localhost:3306/java_studyroom_project?serverTimezone=UTC&characterEncoding=UTF-8";
        String dbUser = "";
        String dbPassword = "";

        Scanner scanner = new Scanner(System.in);

        // 2. 사용자 입력
        System.out.print("아이디 (username): ");
        String username = scanner.nextLine();

        System.out.print("비밀번호 (password): ");
        String password = scanner.nextLine();

        try {
            // 3. JDBC 드라이버 로딩
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 4. DB 연결
            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);

            // 5. SQL 쿼리 준비 (입력한 username과 password가 일치하는지 확인)
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            // 6. 쿼리 실행
            ResultSet rs = pstmt.executeQuery();

            // 7. 결과 확인
            if (rs.next()) {
                System.out.println("로그인 성공! 환영합니다, " + rs.getString("nickname") + "님 :)");
            } else {
                System.out.println("로그인 실패... 아이디 또는 비밀번호를 확인하세요.");
            }

            // 8. 자원 정리
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();
    }
}

/*
PreparedStatement pstmt : SQL 문을 미리 준비해서 실행하는 객체
?는 나중에 값을 채워줄 자리임
pstmt.setString(1, username);   // 첫 번째 ?에 username
pstmt.setString(2, password);   // 두 번째 ?에 password

executeQuery() : Select 쿼리 실행

rs.next() : 결과(ResultSet)의 다음 행으로 이동함
결과가 있으면 true, 없으면 false 반환함
 */

