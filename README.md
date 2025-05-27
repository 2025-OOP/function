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

## 필요 라이브러리

| 라이브러리 파일 | 설명 |
|-----------------|------|
| `mysql-connector-j-9.3.0.jar` | MySQL DB 연동 (JDBC) |
| `gson-2.10.1.jar` | JSON 직렬화/역직렬화 (Google Gson) |

> 아래 두 개의 `.jar` 파일을 프로젝트에 추가해야 정상 동작함

> IntelliJ에서는 `File > Project Structure > Libraries`에 `.jar` 파일 등록 필요

<img width="763" alt="image" src="https://github.com/user-attachments/assets/367965e4-2760-4247-af69-37a5c27237e1" />

<br>

---

<br>

# 📝 API 명세서

## 회원가입

* **URL**: `POST /signup`
* **요청 JSON**:

```json
{
  "username": "owen",
  "password": "0001"
}
```


#### 성공

```json
{
  "status": "success",
  "message": "회원가입 완료"
}
```

#### 실패 (중복 아이디)

```json
{
  "status": "fail",
  "message": "이미 존재하는 아이디입니다."
}
```
<br>

## 로그인

* **URL**: `POST /login`
* **요청 JSON**:

```json
{
  "username": "owen",
  "password": "0001"
}
```


#### 성공

```json
{
  "status": "success",
  "message": "로그인 성공"
}
```

#### 실패

```json
{
  "status": "fail",
  "message": "아이디 또는 비밀번호가 잘못되었습니다."
}
```
<br>

## 로그아웃

* **URL**: `POST /logout`
* **요청 바디 없음**

```json
{
  "status": "success",
  "message": "로그아웃 완료"
}
```

<br>

## 테스트 예시 (curl)

### 회원가입 테스트

```bash
curl -X POST http://localhost:8080/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"owen", "password":"0001"}'
```

### 로그인 테스트

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"owen", "password":"0001"}'
```


### 로그아웃 테스트

```bash
curl -X POST http://localhost:8080/logout
```

---
<br>

## 📂 프로젝트 파일 구성

```
📁 java_study_rooom/
├── UserHttpServer.java        # 서버 실행
├── Signup.java                # 콘솔용 회원가입 (테스트용)
├── Login.java                 # 콘솔용 로그인 (테스트용)
├── Logout.java                # 콘솔용 로그아웃 (형식)
├── DBTest.java                # DB 연결 테스트용
├── user.sql                  # DB 생성 SQL
├── lib/
│   ├── gson-2.10.1.jar
│   └── mysql-connector-j-9.3.0.jar
└── README.md
```



