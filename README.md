# WeStudy 📚
> **친구와 함께 공부하는 화상 스터디 서비스**

WeStudy는 화상 공부방을 통해 친구들과 함께 학습할 수 있는 온라인 스터디 플랫폼입니다. Jitsi Meet을 활용한 화상 채팅, 개인 학습 타이머, 할 일 관리 등 다양한 기능을 제공합니다.

## 주요 기능

### 1. 사용자 시스템
- 회원가입 / 로그인 / 로그아웃
- 마이페이지에서 개인 학습 현황 확인
- 사용자별 학습 시간 누적 관리

### 2. 화상 스터디룸
- **Jitsi Meet 연동**: 실시간 화상 채팅 지원
- **방 생성/관리**: 비밀번호 설정 가능한 공개/비공개 방
- **참가자 관리**: 실시간 참가자 목록 조회 및 중복 입장 방지
- **자동 설정**: 카메라 ON, 마이크 OFF로 기본 설정

### 3. 학습 타이머 & 랭킹
- 개인별 학습 시간 측정 및 저장
- 방 내 참가자들의 학습 시간 랭킹 표시
- 누적 학습 시간 통계 제공

### 4. 할 일 관리
- 개인 투두리스트 작성 및 관리
- 날짜별 과제/공부 계획 등록
- 완료/미완료 상태 관리

## 시스템 구조

```
WeStudy Platform
├── Frontend (React)
├── Backend (Java HTTP Server)
├── Database (MySQL)
└── External API (Jitsi Meet)
```

## 기술 스택

| 분야 | 기술 |
|------|------|
| **Frontend** | React, JavaScript |
| **Backend** | Java HTTP Server (Port 8080) |
| **Database** | MySQL |
| **External API** | Jitsi Meet Embed API |
| **Libraries** | Gson, MySQL Connector |

## 사전 요구사항

- Java JDK 8+
- MySQL 8.0+
- IntelliJ IDEA (권장)

### 필요 라이브러리
- `mysql-connector-j-9.3.0.jar` - MySQL DB 연동
- `gson-2.10.1.jar` - JSON 직렬화/역직렬화

## 설치 및 실행

### 1. 데이터베이스 설정

```sql
-- 데이터베이스 생성
CREATE DATABASE java_studyroom_project;
CREATE DATABASE studyroom;

-- 사용자 테이블
CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255)
);

-- 할 일 테이블
CREATE TABLE todo (
    todo_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    date DATE NOT NULL,
    content TEXT NOT NULL,
    completed BOOLEAN DEFAULT FALSE
);

-- 방 테이블
CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    password VARCHAR(100),
    user_count INT DEFAULT 0
);

-- 방 참가자 테이블
CREATE TABLE room_participants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_id INT NOT NULL,
    username VARCHAR(50) NOT NULL,
    jitsi_room_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
    UNIQUE KEY unique_room_user (room_id, username)
);
```

### 2. 프로젝트 설정

```bash
# 프로젝트 클론
git clone [repository-url]
cd westudy

# IntelliJ에서 라이브러리 등록
# File > Project Structure > Libraries > Add .jar files
```

### 3. 서버 실행

```bash
# 컴파일
javac -cp "lib/*" -d out src/**/*.java

# 실행
java -cp "lib/*;out" usersystem.UserHttpServer
```

서버가 `http://localhost:8080`에서 실행됩니다.

## 📡 API 명세서

### 사용자 관리

#### 회원가입
```http
POST /signup
Content-Type: application/json

{
  "username": "owen",
  "password": "0001"
}
```

#### 로그인
```http
POST /login
Content-Type: application/json

{
  "username": "owen",
  "password": "0001"
}
```

#### 로그아웃
```http
POST /logout
```

### 방 관리

#### 방 입장
```http
POST /api/rooms/{roomId}/enter
```

#### 참가자 목록 조회
```http
GET /api/rooms/{roomId}/participants
```

### 타이머 관리

#### 학습 시간 저장
```http
POST /api/saveTime/{userId}
Content-Type: application/json

{
  "userId": "yein",
  "hour": 1,
  "minute": 30,
  "second": 15
}
```

#### 학습 시간 조회
```http
GET /api/saveTime/{userId}
```

### 할 일 관리

#### 할 일 추가
```http
POST /api/todo
Content-Type: application/json

{
  "user": "owen",
  "date": "2025-05-30",
  "content": "자바 백엔드 과제",
  "completed": false
}
```

#### 할 일 조회
```http
GET /api/todo?user=owen&date=2025-05-30
```

#### 할 일 수정
```http
PUT /api/todo/{todoId}
Content-Type: application/json

{
  "content": "내용 수정",
  "completed": true
}
```

#### 할 일 삭제
```http
DELETE /api/todo/{todoId}
```

#### 마이페이지 조회
```http
GET /api/mypage?username=owen
```



## 프로젝트 구조

```
WeStudy/
├── src/
│   ├── usersystem/
│   │   ├── UserHttpServer.java      # 메인 서버
│   │   ├── api/                     # API 핸들러
│   │   ├── model/                   # 데이터 모델
│   │   ├── service/                 # 비즈니스 로직
│   │   └── utils/                   # 유틸리티
│   ├── rooms/
│   │   ├── RoomController.java      # 방 관리 API
│   │   ├── JitsiMeetService.java    # Jitsi Meet 연동
│   │   └── RoomParticipant.java     # 참가자 모델
│   └── timer/
│       ├── TimerController.java     # 타이머 API
│       ├── TimerService.java        # 타이머 로직
│       └── UserTime.java            # 시간 데이터 모델
├── lib/
│   ├── gson-2.10.1.jar
│   └── mysql-connector-j-9.3.0.jar
├── user.sql                         # DB 초기화 스크립트
└── README.md
```

## 향후 개발 계획

### AI 기능 도입
- **학습 패턴 분석**: 사용자의 방문 로그(요일, 시간대)를 수집하여 맞춤형 학습 어시스턴트 기능 제공
- **스마트 알림**: 평소 학습 시간에 맞춰 개인화된 학습 리마인드 알림
- **학습 효율성 분석**: 집중 시간, 휴식 시간 패턴을 분석하여 최적의 학습 스케줄 제안

### UI/UX 개선
- **관리자 페이지**: 사용자 통계, 방 관리, 시스템 모니터링 전용 화면
- **사용자 대시보드**: 학습 현황, 목표 달성률, 친구들과의 학습 비교 기능
- **반응형 디자인**: 모바일, 태블릿 환경 최적화

## 팀 구성 및 역할

| 이름 | 담당 기능 | 주요 역할 |
|------|-----------|-----------|
| **김민서** | 사용자 시스템 | 회원가입, 로그인, 인증 관리 |
| **박서은** | 화상 채팅 | Jitsi Meet 연동, 참가자 관리 |
| **이세원** | 방 관리 | 방 생성/삭제, 비밀번호 설정 |
| **이서아** | 타이머 & 랭킹 | 학습 시간 측정, 랭킹 시스템 |
| **조예인** | 할 일 관리 | 투두리스트, 마이페이지 |
| **김서윤** | 프로젝트 총괄 | 통합, 문서화, 발표 |

## 협업 과정

### Git 워크플로우
- **브랜치 전략**: 기능별 브랜치 생성 후 main 브랜치로 통합
- **코드 리뷰**: Pull Request를 통한 코드 검토 및 피드백
- **통합 테스트**: 각 기능별 API 연동 테스트 및 전체 시스템 통합 검증

### API 연동
- **RESTful API**: 표준화된 HTTP 메서드와 JSON 통신
- **CORS 설정**: 프론트엔드-백엔드 간 크로스 오리진 요청 처리
- **에러 핸들링**: 일관된 응답 형식과 상태 코드 관리
