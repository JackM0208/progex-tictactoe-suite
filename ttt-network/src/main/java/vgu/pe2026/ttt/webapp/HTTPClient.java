package vgu.pe2026.ttt.webapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.net.URL;
import java.net.HttpURLConnection;
import org.json.JSONObject;

public class HTTPClient {
    
    private static final String SERVER_URL = "http://localhost:5678/play";

    public static void main(String[] args) {
        // if (args.length != 1) {
        //     System.out.println("Please, input a valid option [1-2]");
        //     return;
        // }

        int myId = 1;
        // String boardstate = "START";
        String boardState = "000 000 000";
        // String signature = "";
        // String receivedToken = "";
        // String TTL = "";
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
        
        String userInput = "";

        while(!isEnd){
            
            if(!boardState.equals("START")){
                System.out.print("Please enter your move: ");
                userInput = consoleIn.nextLine();
            }
                 
                // boolean isMoveInvalid = false;

                // if(!boardState.equals("START")){
                //     clOut.println(userInput);
                //     clOut.println(signature);

                //     clOut.println(receivedToken);
                //     clOut.println(TTL);
                // }

                JSONObject json = new JSONObject();
                json.put("ID", 1);
                json.put("STATE", boardState);
                json.put("MOVE", userInput);

                String serverResponse = sendPostRequest(userInput, json);
                
                if(!serverResponse.isEmpty()){
                    JSONObject jsonResponse = new JSONObject(serverResponse);

                    String message = jsonResponse.getString("MESSAGE");
                    boardState = jsonResponse.getString("STATE");
                    boolean isGameOver = jsonResponse.getBoolean("GAME_OVER");
                    
                    System.out.println(message);
                    System.out.println(boardState);

                    if(isGameOver){
                        // System.out.println("Game Over!");
                        isEnd = true;
                }   
            }
        }
    }
    private static String sendPostRequest(String urlString, JSONObject data){
        try
        {
            URL url = new URL(SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(data.toString().getBytes());
            os.flush();
            os.close();

            if(conn.getResponseCode() == 200){
                InputStream in = conn.getInputStream();
                String serverResponse = new BufferedReader(new InputStreamReader(in))
                                        .lines()  
                                        .collect(Collectors.joining("\n"));

                return serverResponse;
            }

            else {
                return "";
            }

        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
        
    }
}