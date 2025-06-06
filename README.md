# 🚀 주요 기능
## 1. Jitsi Meet 화상회의 연동

- 방 입장 시 Jitsi Meet 설정 자동 생성
- 기본 설정: 카메라 ON, 마이크 OFF
- 같은 방 사용자들은 동일한 Jitsi 회의실 공유

## 2. 참가자 관리

- 방별 참가자 username 추적
- 실시간 참가자 목록 조회
- 중복 입장 방지

# 서버 실행 방법
## 1. MySQL 실행 및 DB 생성
- DB 이름: studyroom
- room_participants 테이블 생성:
  ```sql
  CREATE TABLE room_participants (
      id INT AUTO_INCREMENT PRIMARY KEY,
      room_id INT NOT NULL,
      username VARCHAR(50) NOT NULL,
      jitsi_room_name VARCHAR(100) NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE,
      UNIQUE KEY unique_room_user (room_id, username)
  );

 - room 테이블 생성(세원이도 만든건가..? 만들어져 있는거 걍 쓰면 됨)
```sql
CREATE TABLE rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    password VARCHAR(100),
    user_count INT DEFAULT 0
);
```

## 2. Java 서버 실행
- Main.java 실행

- 서버 포트: 8080

# Postman 테스트 화면



## 필요 라이브러리

| 파일명 | 설명 |
|--------|------|
| `mysql-connector-j-9.3.0.jar` | MySQL DB 연동용 JDBC 드라이버 |
| `gson-2.10.1.jar` | JSON 직렬화/역직렬화 |

※ IntelliJ의 경우 File > Project Structure > Libraries에서 .jar 등록 필요

## API 명세서
- POST /api/rooms/{roomId}/enter - 방 입장
- GET /api/rooms/{roomId}/participants - 참가자 목록

# 김서윤님께..^^
html은 프론트에서 하는건가요? 개판 쳐놔서 미리 미안하다는 편지를 씁니다.

- Jitsi Meet 라이브러리 추가
html<script src='https://meet.jit.si/external_api.js'></script>

- CORS 처리 (이미 백엔드에서 허용함)
roomId와 username 준비

```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>WeStudy Jitsi Test</title>
    <style>
        html, body, #meet { height: 100%; margin: 0; padding: 0; }
    </style>
    <script src="https://meet.jit.si/external_api.js"></script>
</head>
<body>
<div id="meet"></div>
<script>
    const domain = 'meet.jit.si';
    const options = {
        roomName: 'StudyRoom_TestRoom_12345',  // 실제 방 이름 입력
        width: '100%',
        height: '100%',
        parentNode: document.querySelector('#meet'),
        userInfo: {
            displayName: 'TestUser'  // 실제 사용자명 입력
        },
        configOverwrite: {
            startWithAudioMuted: true,
            startWithVideoMuted: false,
            prejoinPageEnabled: false
        },
        interfaceConfigOverwrite: {
            SHOW_JITSI_WATERMARK: false,
            SHOW_BRAND_WATERMARK: false,
            TOOLBAR_BUTTONS: [
                'microphone', 'camera', 'desktop',
                'fullscreen', 'hangup', 'tileview'
            ]
        }
    };
    const api = new JitsiMeetExternalAPI(domain, options);
</script>
</body>
</html>

```
저는 이런식으로 테스트 하긴 했네요

# 📂 프로젝트 파일 구성
``` 
src/
├── db/
│   └── DB.java                          # 데이터베이스 연결
├── rooms/
│   ├── RoomController.java              # API 엔드포인트 (수정)
│   ├── RoomParticipant.java            # 참가자 모델 
│   ├── RoomParticipantRepository.java   # 참가자 DB 작업 
│   ├── JitsiMeetService.java           # Jitsi Meet 비즈니스 로직 
│   ├── JitsiMeetConfig.java            # Jitsi 설정 
│   └── JsonUtils.java                   # JSON 유틸리티
└── Main.java                            # 서버 진입점
```
