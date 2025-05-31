import com.domain.studyroom.todo.TodoController;
import com.domain.studyroom.users.UserHttpServer;
import com.sun.net.httpserver.HttpServer;
import com.domain.studyroom.rooms.RoomController;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/rooms", new RoomController());

        TodoController todoController = new TodoController();
        server.createContext("/api/todo", todoController);
        server.createContext("/api/todo/update", todoController);
        server.createContext("/api/todo/delete", todoController);

        server.createContext("/signup", new UserHttpServer.SignupHandler());
        server.createContext("/login", new UserHttpServer.LoginHandler());
        server.createContext("/logout", new UserHttpServer.Logout());

        server.setExecutor(null);
        server.start();
        System.out.println("서버 시작됨: http://localhost:8080");
    }
}
