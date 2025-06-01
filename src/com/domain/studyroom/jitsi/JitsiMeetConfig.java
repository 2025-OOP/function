package com.domain.studyroom.jitsi;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class JitsiMeetConfig {

    private static final String JITSI_DOMAIN = "meet.jit.si";

    /**
     * Jitsi Meet 설정 생성
     * @param roomName 사용자가 입력한 방 이름
     * @param username 사용자 이름
     * @return Jitsi Meet 설정 JSON
     */
    public static JsonObject createConfig(String roomName, String username) {
        JsonObject config = new JsonObject();

        // 사용자가 입력한 방 이름 사용 (공백을 언더스코어로 변경)
        String RoomName = roomName.replaceAll("\\s", "_");

        config.addProperty("domain", JITSI_DOMAIN);
        config.addProperty("roomName", RoomName);

        // 사용자 정보
        JsonObject userInfo = new JsonObject();
        userInfo.addProperty("displayName", username);
        config.add("userInfo", userInfo);

        // 인터페이스 설정
        JsonObject interfaceConfig = new JsonObject();
        interfaceConfig.addProperty("SHOW_JITSI_WATERMARK", false);
        interfaceConfig.addProperty("SHOW_BRAND_WATERMARK", false);
        interfaceConfig.addProperty("DISABLE_VIDEO_BACKGROUND", false);
        interfaceConfig.addProperty("MOBILE_APP_PROMO", false);
        interfaceConfig.addProperty("SHOW_CHROME_EXTENSION_BANNER", false);

        // 툴바 버튼 설정
        JsonArray toolbarButtons = new JsonArray();
        toolbarButtons.add("microphone");
        toolbarButtons.add("camera");
        toolbarButtons.add("desktop");
        toolbarButtons.add("fullscreen");
        toolbarButtons.add("hangup");
        toolbarButtons.add("tileview");
        toolbarButtons.add("settings");
        interfaceConfig.add("TOOLBAR_BUTTONS", toolbarButtons);

        config.add("interfaceConfigOverwrite", interfaceConfig);

        // 기본 설정 (카메라 on, 마이크 off)
        JsonObject configOverwrite = new JsonObject();
        configOverwrite.addProperty("startWithAudioMuted", true);  // 마이크 off
        configOverwrite.addProperty("startWithVideoMuted", false); // 카메라 on
        configOverwrite.addProperty("disableDeepLinking", true);
        configOverwrite.addProperty("prejoinPageEnabled", false);
        configOverwrite.addProperty("enableWelcomePage", false);
        configOverwrite.addProperty("enableClosePage", false);

        config.add("configOverwrite", configOverwrite);

        return config;
    }

    /**
     * 기존 Jitsi 방에 참가하기 위한 설정
     * @param roomName 기존 방 이름
     * @param username 사용자 이름
     * @return Jitsi Meet 설정 JSON
     */
    public static JsonObject createConfigForExistingRoom(String roomName, String username) {
        JsonObject config = new JsonObject();

        config.addProperty("domain", JITSI_DOMAIN);
        config.addProperty("roomName", roomName);

        // 사용자 정보
        JsonObject userInfo = new JsonObject();
        userInfo.addProperty("displayName", username);
        config.add("userInfo", userInfo);

        // 인터페이스 설정
        JsonObject interfaceConfig = new JsonObject();
        interfaceConfig.addProperty("SHOW_JITSI_WATERMARK", false);
        interfaceConfig.addProperty("SHOW_BRAND_WATERMARK", false);

        JsonArray toolbarButtons = new JsonArray();
        toolbarButtons.add("microphone");
        toolbarButtons.add("camera");
        toolbarButtons.add("desktop");
        toolbarButtons.add("fullscreen");
        toolbarButtons.add("hangup");
        toolbarButtons.add("tileview");
        toolbarButtons.add("settings");
        interfaceConfig.add("TOOLBAR_BUTTONS", toolbarButtons);

        config.add("interfaceConfigOverwrite", interfaceConfig);

        // 기본 설정
        JsonObject configOverwrite = new JsonObject();
        configOverwrite.addProperty("startWithAudioMuted", true);
        configOverwrite.addProperty("startWithVideoMuted", false);
        configOverwrite.addProperty("prejoinPageEnabled", false);

        config.add("configOverwrite", configOverwrite);

        return config;
    }
}