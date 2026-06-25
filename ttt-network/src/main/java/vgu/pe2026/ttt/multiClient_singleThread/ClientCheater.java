package vgu.pe2026.ttt.multiClient_singleThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientCheater {
    private static final String SERVER_NAME = "localhost";
    private static final int SERVER_PORT = 5678;

    public static void main(String[] args) {
        // if (args.length != 1) {
        //     System.out.println("Please, input a valid option [1-2]");
        //     return;
        // }

        int myId = 1;
        String boardState = "START";
        String signature = "";
        boolean isEnd = false;
        Scanner consoleIn = new Scanner(System.in);

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
        

        while(!isEnd){
            String userInput = "";

            if(!boardState.equals("START")){
                System.out.print("Please enter your move: ");
                userInput = consoleIn.nextLine();

                boardState = "110 000 000";
                signature = "GG0g9uAi3QPFyOjLgBDcScAX+yRnVZjqQCFkUh4Ss9I=";
            }
            
            try (
                Socket socket = new Socket(SERVER_NAME, SERVER_PORT);
                BufferedReader clIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter clOut = new PrintWriter(socket.getOutputStream(), true);)
            {
                
                clOut.println(myId);         
                clOut.println(boardState);    
                // boolean isMoveInvalid = false;

                if(!boardState.equals("START")){
                    clOut.println(userInput);
                    clOut.println(signature);
                }

                String serverMessage;
                
                while ((serverMessage = clIn.readLine()) != null) {
                    
                    if (serverMessage.startsWith("STATE:")) {

                        boardState = serverMessage.substring(6);
                        // System.out.println("State received from Server: " + boardState);
                    } 
                    else if(serverMessage.startsWith("SIG")){
                        signature = serverMessage.substring(4);
                        // System.out.println("[DEBUG CLIENT] Signature received from server: [" + signature + "]");
                    }
                    
                    else if (serverMessage.equals("INVALID_MOVE")) {
                        System.out.println("This move is invalid.");
                        // isMoveInvalid = true;
                    } 
                    else if (serverMessage.equals("GAME_OVER")) {
                        isEnd = true;
                        consoleIn.close();
                    } 

                    // if (isMoveInvalid) {
                    //     continue;
                    // }

                    else {
                        System.out.println(serverMessage);
                    }
                }

            }   catch (IOException e) {
                    System.err.println("Disconnected from Server: " + e.getMessage());
                    break;
                }   
        }
        
    }
}