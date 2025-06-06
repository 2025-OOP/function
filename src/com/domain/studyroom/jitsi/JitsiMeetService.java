package com.domain.studyroom.jitsi;

import com.domain.studyroom.rooms.RoomRepository;
import com.google.gson.JsonObject;

import java.sql.SQLException;
import java.util.List;

public class JitsiMeetService {

    private final RoomParticipantRepository participantRepo = new RoomParticipantRepository();
    private final RoomRepository roomRepo = new RoomRepository();

    /**
     * 방 입장 처리 (Jitsi Meet 설정 포함)
     */
    public JsonObject enterRoomWithJitsi(int roomId, String username) throws SQLException, ClassNotFoundException {
        JsonObject response = new JsonObject();

        // 이미 방에 있다면 기존 참가자 정보 정리 후 재입장
        if (participantRepo.isUserInRoom(roomId, username)) {
            leaveRoomWithCleanup(roomId, username); // 기존 참가자 정보 삭제 후 계속 진행
        }

        // 방 존재 확인
        int userCount = roomRepo.getUserCount(roomId);
        if (userCount == -1) {
            response.addProperty("success", false);
            response.addProperty("message", "존재하지 않는 방입니다.");
            return response;
        }

        // 기존 Jitsi 방 이름 확인
        String jitsiRoomName = participantRepo.getJitsiRoomName(roomId);

        // Jitsi 설정 생성
        JsonObject jitsiConfig;
        if (jitsiRoomName == null) {
            jitsiConfig = JitsiMeetConfig.createConfig("Room" + roomId, username);
            jitsiRoomName = jitsiConfig.get("roomName").getAsString();
        } else {
            jitsiConfig = JitsiMeetConfig.createConfigForExistingRoom(jitsiRoomName, username);
        }

        // 참여자 추가
        participantRepo.addParticipant(roomId, username, jitsiRoomName);

        // user_count 증가
        roomRepo.enterRoom(roomId);

        // 응답 구성
        response.addProperty("success", true);
        response.addProperty("message", "입장 성공");
        response.add("jitsiConfig", jitsiConfig);

        return response;
    }

    /**
     * 방 퇴장 처리
     */
    public boolean leaveRoomWithCleanup(int roomId, String username) throws SQLException, ClassNotFoundException {
        // 참가자 제거
        participantRepo.removeParticipant(roomId, username);

        // user_count 감소
        return roomRepo.leaveRoom(roomId);
    }

    /**
     * 방 참가자 username 목록 조회
     */
    public List<String> getParticipantUsernames(int roomId) throws SQLException, ClassNotFoundException {
        return participantRepo.getParticipantUsernames(roomId);
    }

    /**
     * 방 자동 삭제 시 참가자 정보도 정리
     */
    public boolean autoDeleteRoom(int roomId) throws SQLException, ClassNotFoundException {
        if (roomRepo.getUserCount(roomId) == 0) {
            participantRepo.removeAllParticipants(roomId);
            return roomRepo.autoDeleteRoom(roomId);
        }
        return false;
    }
}
