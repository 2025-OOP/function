Timer 기능 API 명세서
Base URL: http://localhost:8080/api/saveTime/{userId}

1. 사용자 타이머 저장
URL: /api/saveTime/{userId}

Method: POST

설명: 해당 사용자의 타이머(시간 정보)를 서버에 저장합니다.

요청 본문 형식 (application/json):

{
  "userId": "yein",
  "hour": 1,
  "minute": 30,
  "second": 15
}
성공 응답:

Status: 200 OK

Body: "시간 저장 완료"

2. 사용자 타이머 조회
URL: /api/saveTime/{userId}

Method: GET

설명: 해당 사용자의 누적된 타이머 시간을 조회합니다.

응답 본문 형식 (application/json):

{
  "userId": "yein",
  "hour": 2,
  "minute": 10,
  "second": 40
}
