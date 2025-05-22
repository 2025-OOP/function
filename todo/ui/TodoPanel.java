package studyapp.todo.ui;

import studyapp.todo.model.Todo;
import studyapp.todo.storage.TodoStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TodoPanel extends JPanel {
    private DefaultListModel<String> listModel;
    private JList<String> todoList;

    public TodoPanel() {
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        todoList = new JList<>(listModel);

        JTextField input = new JTextField();
        JButton addButton = new JButton("추가");
        JButton deleteButton = new JButton("삭제");

        // [추가] 버튼 클릭 시
        addButton.addActionListener((ActionEvent e) -> {
            String text = input.getText().trim();
            if (!text.isEmpty()) {
                TodoStorage.addTodo(new Todo(text));
                refreshList();
                input.setText("");
            }
        });

        // [삭제] 버튼 클릭 시
        deleteButton.addActionListener((ActionEvent e) -> {
            int selectedIndex = todoList.getSelectedIndex();
            if (selectedIndex != -1) {
                TodoStorage.deleteTodo(selectedIndex);
                refreshList();
            }
        });

        // [더블 클릭 시 완료 토글]
        todoList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = todoList.locationToIndex(evt.getPoint());
                    if (index != -1) {
                        TodoStorage.toggleComplete(index);
                        refreshList();
                    }
                }
            }
        });

        // 상단 입력창 + 추가 버튼
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(input, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        // 하단 삭제 버튼
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(deleteButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(todoList), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshList();
    }

    // 리스트 다시 그리기
    private void refreshList() {
        listModel.clear();
        for (Todo t : TodoStorage.getTodoList()) {
            listModel.addElement((t.isCompleted() ? "[완료] " : "[ ] ") + t.getContent());
        }
    }
}
