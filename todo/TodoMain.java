package studyapp.todo;

import studyapp.todo.ui.TodoPanel;
import studyapp.todo.ui.MypagePanel;

import javax.swing.*;

public class TodoMain {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JFrame frame = new JFrame("투두리스트 & 마이페이지");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null); // 화면 중앙 정렬

        // 탭 구성
        JTabbedPane tabbedPane = new JTabbedPane();
        TodoPanel todoPanel = new TodoPanel();
        MypagePanel mypagePanel = new MypagePanel();

        tabbedPane.addTab("To-do List", todoPanel);
        tabbedPane.addTab("My Page", mypagePanel);

        // 탭 전환 시 마이페이지 자동 새로고침
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == mypagePanel) {
                mypagePanel.refresh();
            }
        });

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
}
