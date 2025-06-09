package com.domain.studyroom.jitsi;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class JitsiMeetConfig {

    private static final String JITSI_DOMAIN = "jitsi.riot.im"; // meet.jit.si → jitsi.riot.im

    /**
     * 방 생성자용 (moderator) 설정 - 호스트 대기 없이 바로 입장
     */
    public static JsonObject createConfigAsModerator(String userRoomName, String username) {
        JsonObject config = new JsonObject();

        // 사용자 방 이름을 기반으로 Jitsi 연결용 고유 방 이름 생성
        String jitsiRoomName = "public-" + sanitizeRoomName(userRoomName) + "-" + System.currentTimeMillis();

        config.addProperty("domain", JITSI_DOMAIN);
        config.addProperty("roomName", jitsiRoomName); // Jitsi 연결용 고유 이름
        config.addProperty("displayRoomName", userRoomName); // 사용자가 보는 이름

        // 사용자 정보
        JsonObject userInfo = new JsonObject();
        userInfo.addProperty("displayName", username);
        config.add("userInfo", userInfo);

        // 호스트 대기 완전 우회 설정
        JsonObject configOverwrite = new JsonObject();
        configOverwrite.addProperty("startWithAudioMuted", false); // 방장은 음소거 해제
        configOverwrite.addProperty("startWithVideoMuted", false);
        configOverwrite.addProperty("enableWelcomePage", false);
        configOverwrite.addProperty("prejoinPageEnabled", false);
        configOverwrite.addProperty("requireDisplayName", false);
        configOverwrite.addProperty("disableDeepLinking", true);

        // 핵심: 호스트 대기 완전 우회
        configOverwrite.addProperty("enableJoinBeforeHost", true);
        configOverwrite.addProperty("enableUserRolesBasedOnToken", false);
        configOverwrite.addProperty("enableLobby", false);
        configOverwrite.addProperty("lobbyEnabled", false);
        configOverwrite.addProperty("enableLobbyChat", false);
        configOverwrite.addProperty("lobbyPasswordEnabled", false);
        configOverwrite.addProperty("disableModeratorIndicator", false); // 방장은 moderator 표시

        // 추가 보안 우회 설정
        configOverwrite.addProperty("enableInsecureRoomNameWarning", false);
        configOverwrite.addProperty("enableEmailInStats", false);
        configOverwrite.addProperty("enableAutomaticUrlCopy", false);
        configOverwrite.addProperty("enableModeratorOnlyMessage", false);
        configOverwrite.addProperty("enableGuestDomain", false);

        config.add("configOverwrite", configOverwrite);

        // 인터페이스 설정
        JsonObject interfaceConfig = new JsonObject();
        interfaceConfig.addProperty("SHOW_JITSI_WATERMARK", false);
        interfaceConfig.addProperty("SHOW_BRAND_WATERMARK", false);
        interfaceConfig.addProperty("DISABLE_JOIN_LEAVE_NOTIFICATIONS", true);
        interfaceConfig.addProperty("HIDE_DEEP_LINKING_LOGO", true);
        interfaceConfig.addProperty("HIDE_INVITE_MORE_HEADER", true);

        // 방장용 툴바 (모든 기능)
        JsonArray toolbarButtons = new JsonArray();
        toolbarButtons.add("microphone");
        toolbarButtons.add("camera");
        toolbarButtons.add("desktop");
        toolbarButtons.add("fullscreen");
        toolbarButtons.add("hangup");
        toolbarButtons.add("tileview");
        toolbarButtons.add("chat");
        toolbarButtons.add("raisehand");
        toolbarButtons.add("settings");
        interfaceConfig.add("TOOLBAR_BUTTONS", toolbarButtons);

        config.add("interfaceConfigOverwrite", interfaceConfig);

        return config;
    }

    /**
     * 일반 참가자용 설정 - 호스트 대기 없이 바로 입장
     */
    public static JsonObject createConfigAsParticipant(String userRoomName, String username, String existingJitsiRoomName) {
        JsonObject config = new JsonObject();

        config.addProperty("domain", JITSI_DOMAIN);
        config.addProperty("roomName", existingJitsiRoomName); // 기존 Jitsi 방 이름 사용
        config.addProperty("displayRoomName", userRoomName); // 사용자가 보는 이름

        // 사용자 정보
        JsonObject userInfo = new JsonObject();
        userInfo.addProperty("displayName", username);
        config.add("userInfo", userInfo);

        // 호스트 대기 완전 우회 설정
        JsonObject configOverwrite = new JsonObject();
        configOverwrite.addProperty("startWithAudioMuted", true); // 일반 참가자는 음소거로 시작
        configOverwrite.addProperty("startWithVideoMuted", false);
        configOverwrite.addProperty("enableWelcomePage", false);
        configOverwrite.addProperty("prejoinPageEnabled", false);
        configOverwrite.addProperty("requireDisplayName", false);
        configOverwrite.addProperty("disableDeepLinking", true);

        // 핵심: 호스트 대기 완전 우회
        configOverwrite.addProperty("enableJoinBeforeHost", true);
        configOverwrite.addProperty("enableUserRolesBasedOnToken", false);
        configOverwrite.addProperty("enableLobby", false);
        configOverwrite.addProperty("lobbyEnabled", false);
        configOverwrite.addProperty("enableLobbyChat", false);
        configOverwrite.addProperty("lobbyPasswordEnabled", false);
        configOverwrite.addProperty("disableModeratorIndicator", true); // 일반 참가자는 moderator 표시 숨김

        // 추가 보안 우회 설정
        configOverwrite.addProperty("enableInsecureRoomNameWarning", false);
        configOverwrite.addProperty("enableEmailInStats", false);
        configOverwrite.addProperty("enableAutomaticUrlCopy", false);
        configOverwrite.addProperty("enableModeratorOnlyMessage", false);
        configOverwrite.addProperty("enableGuestDomain", false);

        config.add("configOverwrite", configOverwrite);

        // 인터페이스 설정
        JsonObject interfaceConfig = new JsonObject();
        interfaceConfig.addProperty("SHOW_JITSI_WATERMARK", false);
        interfaceConfig.addProperty("SHOW_BRAND_WATERMARK", false);
        interfaceConfig.addProperty("DISABLE_JOIN_LEAVE_NOTIFICATIONS", true);
        interfaceConfig.addProperty("HIDE_DEEP_LINKING_LOGO", true);
        interfaceConfig.addProperty("HIDE_INVITE_MORE_HEADER", true);

        // 일반 참가자용 툴바 (기본 기능만)
        JsonArray toolbarButtons = new JsonArray();
        toolbarButtons.add("microphone");
        toolbarButtons.add("camera");
        toolbarButtons.add("desktop");
        toolbarButtons.add("fullscreen");
        toolbarButtons.add("hangup");
        toolbarButtons.add("tileview");
        toolbarButtons.add("chat");
        toolbarButtons.add("raisehand");
        interfaceConfig.add("TOOLBAR_BUTTONS", toolbarButtons);

        config.add("interfaceConfigOverwrite", interfaceConfig);

        return config;
    }

    /**
     * 방 이름을 Jitsi 연결에 안전한 형태로 변환
     */
    private static String sanitizeRoomName(String roomName) {
        if (roomName == null) return "room";

        // 공백과 특수문자를 제거하고 영문/숫자만 남김
        return roomName.replaceAll("[^a-zA-Z0-9가-힣]", "")
                .replaceAll("\\s+", "")
                .toLowerCase();
    }
}