package vgu.pe2026.ttt.net_threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_NAME = "localhost";
    private static final int SERVER_PORT = 5678;

    public static void main(String[] args) {
        // if (args.length != 1) {
        //     System.out.println("Please, input a valid option [1-2]");
        //     return;
        // }

        int myId = 1;

        // try {
        //     myId = Integer.parseInt(args[0]);
        //     if (myId != 1 && myId != 2) {
        //         System.out.println("Please, input a valid option [1-2]");
        //         return;
        //     }
        // } catch (NumberFormatException e) {
        //     System.out.println("Please, input a valid option [1-2]");
        //     return;
        // }

        try (
            Socket socket = new Socket(SERVER_NAME, SERVER_PORT);
            BufferedReader clIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter clOut = new PrintWriter(socket.getOutputStream(), true);
            Scanner consoleIn = new Scanner(System.in) 
        ) {
            
            clOut.println(myId);

            String serverMessage;
            
            while ((serverMessage = clIn.readLine()) != null) {
                
                if (serverMessage.equals("TURN:" + myId)) {

                    String userInput = consoleIn.nextLine(); 
                    clOut.println(userInput);
                } 
                else if (serverMessage.equals("GAME_OVER")) {
                    break;
                } 
                else {
                    System.out.println(serverMessage);
                }
            }

        } catch (IOException e) {
            System.err.println("Lost connection: " + e.getMessage());
        }
    }
}