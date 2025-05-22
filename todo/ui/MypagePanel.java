package studyapp.todo.ui;

import studyapp.todo.model.Todo;
import studyapp.todo.storage.TodoStorage;

import javax.swing.*;
import java.awt.*;

public class MypagePanel extends JPanel {

    private JLabel countLabel;
    private JLabel completeLabel;
    private JLabel studyTimeLabel;

    public MypagePanel() {
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel title = new JLabel("마이페이지", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        countLabel = new JLabel();
        completeLabel = new JLabel();
        studyTimeLabel = new JLabel("오늘 공부 시간: 120분", SwingConstants.CENTER); // 임시 값

        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        completeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        studyTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(title);
        add(countLabel);
        add(completeLabel);
        add(studyTimeLabel);

        refresh();
    }

    // 리스트가 변경되었을 때 갱신할 수 있도록 외부에서 호출 가능
    public void refresh() {
        int total = TodoStorage.getTodoList().size();
        int completed = (int) TodoStorage.getTodoList().stream().filter(Todo::isCompleted).count();

        countLabel.setText("총 할 일 수: " + total);
        completeLabel.setText("완료한 할 일 수: " + completed);
    }
}
