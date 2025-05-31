package com.domain.studyroom.timer;

import java.util.ArrayList;
import java.util.List;

public class TimerService {

    private final List<UserTime> userTimes = new ArrayList<>();

    // 새 사용자 등록 (0초)
    public void addUser(String userId) {
        userTimes.add(new UserTime(userId, 0, 0, 0));
    }

    // 사용자 시간 누적 저장
    public void addOrUpdateUserTime(UserTime newTime) {
        for (UserTime ut : userTimes) {
            if (ut.getUserId().equals(newTime.getUserId())) {
                int totalSeconds = ut.getTotalSeconds() + newTime.getTotalSeconds();
                int hour = totalSeconds / 3600;
                int minute = (totalSeconds % 3600) / 60;
                int second = totalSeconds % 60;

                userTimes.remove(ut);
                userTimes.add(new UserTime(newTime.getUserId(), hour, minute, second));
                return;
            }
        }
        userTimes.add(newTime);
    }

    // 특정 사용자 시간 조회
    public UserTime getUserTime(String userId) {
        for (UserTime ut : userTimes) {
            if (ut.getUserId().equals(userId)) {
                return ut;
            }
        }
        return new UserTime(userId, 0, 0, 0);
    }

    // 전체 사용자 시간 리스트 반환 (필요 시)
    public List<UserTime> getAllUserTimes() {
        return new ArrayList<>(userTimes);
    }
}
