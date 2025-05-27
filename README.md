# ⚙️ 서버 실행 방법

### 1. MySQL 실행 및 DB 생성
- PHPMyAdmin 또는 CLI에서 `user.sql` 실행
  - DB 이름: `java_studyroom_project`
  - 테이블: `user`

### 2. Java 서버 실행
- `UserHttpServer.java` 실행
- 서버 포트: `8080`
<br>

---

<br>

## 필요 라이브러리

| 라이브러리 | 용도 | 설치 방법 |
|------------|------|-----------|
| `Gson` (`com.google.code.gson:gson:2.10.1`) | JSON 직렬화/역직렬화 | gson-2.10.1.jar 추가 |
| `MySQL Connector/J` (`mysql:mysql-connector-java:8.0.33`) | JDBC DB 연결용 | mysql-connector-java-8.x.x.jar 추가 |

> IntelliJ에서는 `File > Project Structure > Libraries`에 `.jar` 파일 등록 필요

<img width="763" alt="image" src="https://github.com/user-attachments/assets/367965e4-2760-4247-af69-37a5c27237e1" />
