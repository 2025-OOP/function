package com.domain.studyroom.timer;

import java.util.List;

public class TimerController {

    private TimerService timerService = new TimerService();

    // 사용자 추가 요청 처리
    public void addUser(String userId) {
        timerService.addUser(userId);
    }

    // 모든 사용자 시간 조회
    public List<UserTime> getAllUserTimes() {
        return timerService.getAllUserTimes();
    }

    // 테스트용 main 메서드 예시
    public static void main(String[] args) {
        TimerController controller = new TimerController();

        controller.addUser("user1");
        controller.addUser("user2");

        List<UserTime> userTimes = controller.getAllUserTimes();
        for (UserTime ut : userTimes) {
            System.out.println(ut);
        }
    }
}
