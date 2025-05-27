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

| 라이브러리 파일 | 설명 |
|-----------------|------|
| `mysql-connector-j-9.3.0.jar` | MySQL DB 연동 (JDBC) |
| `gson-2.10.1.jar` | JSON 직렬화/역직렬화 (Google Gson) |

> 아래 두 개의 `.jar` 파일을 프로젝트에 추가해야 정상 동작함
> IntelliJ에서는 `File > Project Structure > Libraries`에 `.jar` 파일 등록 필요

<img width="763" alt="image" src="https://github.com/user-attachments/assets/367965e4-2760-4247-af69-37a5c27237e1" />
