package vgu.pe2026.ttt.multiClient_singleThread.Server_side;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 5678; 
    // private static final int MAX_THREADS = 10;

    public static void main(String[] args) {
        System.out.println("Server is running on port: " + 5678);


        // ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while(true){

                Socket clientSocket = serverSocket.accept(); 

                Game tictactoe = new Game(clientSocket);
                tictactoe.start(); 
            }

        } catch (IOException e) {
            System.err.println("Error " + e.getMessage());
        }
        
    }
}