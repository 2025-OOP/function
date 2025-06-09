package com.domain.studyroom.jitsi;

import com.domain.studyroom.rooms.RoomRepository;
import com.google.gson.JsonObject;

import java.sql.SQLException;
import java.util.List;

public class JitsiMeetService {

    private final RoomParticipantRepository participantRepo = new RoomParticipantRepository();
    private final RoomRepository roomRepo = new RoomRepository();

    /**
     * 방 입장 처리 (Jitsi Meet 설정 포함) - 호스트 대기 없이 바로 입장
     */
    public JsonObject enterRoomWithJitsi(int roomId, String username) throws SQLException, ClassNotFoundException {
        JsonObject response = new JsonObject();

        // 이미 방에 있다면 기존 참가자 정보 정리 후 재입장
        if (participantRepo.isUserInRoom(roomId, username)) {
            leaveRoomWithCleanup(roomId, username);
        }

        // 방 존재 확인
        int userCount = roomRepo.getUserCount(roomId);
        if (userCount == -1) {
            response.addProperty("success", false);
            response.addProperty("message", "존재하지 않는 방입니다.");
            return response;
        }

        // 방 정보 가져오기
        String userRoomName = roomRepo.getRoomName(roomId); // 사용자가 입력한 방 이름
        String roomCreator = roomRepo.getRoomCreator(roomId); // 방 생성자
        boolean isCreator = username.equals(roomCreator);

        // 기존 Jitsi 방 이름 확인 (DB에 저장된 고유 방 이름)
        String existingJitsiRoomName = participantRepo.getJitsiRoomName(roomId);

        // Jitsi 설정 생성
        JsonObject jitsiConfig;
        String jitsiRoomName;

        if (existingJitsiRoomName == null) {
            // 새 방 생성 시 (첫 번째 참가자)
            if (isCreator) {
                // 방장은 새로운 고유 방 이름으로 생성
                jitsiConfig = JitsiMeetConfig.createConfigAsModerator(userRoomName, username);
                jitsiRoomName = jitsiConfig.get("roomName").getAsString();
            } else {
                // 첫 번째 참가자가 방장이 아닌 경우 (비정상적 상황)
                jitsiConfig = JitsiMeetConfig.createConfigAsModerator(userRoomName, username);
                jitsiRoomName = jitsiConfig.get("roomName").getAsString();
            }
        } else {
            // 기존 방 입장 시
            jitsiRoomName = existingJitsiRoomName;
            if (isCreator) {
                // 방장이 재입장하는 경우
                jitsiConfig = JitsiMeetConfig.createConfigAsModerator(userRoomName, username);
                jitsiConfig.addProperty("roomName", jitsiRoomName); // 기존 Jitsi 방 이름 유지
            } else {
                // 일반 참가자 입장
                jitsiConfig = JitsiMeetConfig.createConfigAsParticipant(userRoomName, username, jitsiRoomName);
            }
        }

        // 참여자 추가 (Jitsi 방 이름으로 저장)
        participantRepo.addParticipant(roomId, username, jitsiRoomName);

        // user_count 증가
        roomRepo.enterRoom(roomId);

        // 응답 구성
        response.addProperty("success", true);
        response.addProperty("message", "입장 성공");
        response.addProperty("isHost", isCreator); // 호스트 여부
        response.addProperty("userRoomName", userRoomName); // 사용자가 보는 방 이름
        response.addProperty("jitsiRoomName", jitsiRoomName); // Jitsi 연결용 방 이름
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