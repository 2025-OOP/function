import com.sun.net.httpserver.HttpServer;
import com.domain.studyroom.rooms.RoomController;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/rooms", new RoomController());
        server.setExecutor(null);
        server.start();
        System.out.println("서버 시작됨: http://localhost:8080");
    }
}
