package vgu.pe2026.ttt.webapp.Server_side;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

public class Server {
    private static final int PORT = 5678; 

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/play", new GameHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server is running on port: " + PORT);
    }
}