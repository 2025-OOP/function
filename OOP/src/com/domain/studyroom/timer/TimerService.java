package com.domain.studyroom.timer;

import java.util.ArrayList;
import java.util.List;

public class TimerService {

    private List<UserTime> userTimes = new ArrayList<>();

    // 사용자 추가 (시간은 0으로 초기화)
    public void addUser(String userId) {
        UserTime newUser = new UserTime(userId, 0, 0, 0);
        userTimes.add(newUser);
    }

    // 사용자 시간 추가
    public void addUserTime(UserTime userTime) {
        userTimes.add(userTime);
    }

    // 모든 사용자 시간 반환
    public List<UserTime> getAllUserTimes() {
        return new ArrayList<>(userTimes);
    }
}
