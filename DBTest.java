import java.sql.*;

public class DBTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/java_studyroom_project"; // DB 주소
        String user = "root"; // 사용자명
        String password = "0107"; // 비밀번호

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
                System.out.println("사용자 ID: " + rs.getInt("user_id") + ", 닉네임: " + rs.getString("nickname"));
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
