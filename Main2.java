import com.domain.studyroom.timer.TimerController;
import com.domain.studyroom.todo.MypageController;
import com.domain.studyroom.todo.TodoController;
import com.domain.studyroom.users.UserHttpServer;
import com.sun.net.httpserver.HttpServer;
import com.domain.studyroom.jitsi.RoomControllerMod;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Jitsi + RoomController 기능 통합된 커스텀 컨트롤러
        server.createContext("/api/rooms", new RoomControllerMod());

        // Todo 관련 컨트롤러
        TodoController todoController = new TodoController();
        server.createContext("/api/todo", todoController);
        server.createContext("/api/todo/update", todoController);
        server.createContext("/api/todo/delete", todoController);

        // 마이페이지
        server.createContext("/api/mypage", new MypageController());

        // 사용자 로그인/회원가입/로그아웃
        server.createContext("/api/signup", new UserHttpServer.SignupHandler());
        server.createContext("/api/login", new UserHttpServer.LoginHandler());
        server.createContext("/api/logout", new UserHttpServer.Logout());

        // 타이머 저장
        server.createContext("/api/saveTime", new TimerController());

        server.setExecutor(null); // 기본 executor 사용
        server.start();

        System.out.println("서버 시작됨: http://localhost:8080");
    }
}
