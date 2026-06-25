package vgu.pe2026.ttt.webapp.Server_side;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class GameHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {

            String requestBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody())).lines().collect(Collectors.joining("\n"));
            JSONObject jsonRequest = new JSONObject(requestBody);

            System.out.println("Received: " + jsonRequest.toString());

            int id = jsonRequest.getInt("ID");
            String boardState = jsonRequest.getString("STATE");
            int move = jsonRequest.getInt("MOVE");

            Game tictactoe = new Game();
            String gameResult = tictactoe.start(id, boardState, move);

            System.out.println("Response from Game: " + gameResult);

            String[] parts = gameResult.split("\n");
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("STATUS", "success");
            jsonResponse.put("MESSAGE", parts[0]);

            jsonResponse.put("STATE", parts[parts.length - 1].replace("STATE:", ""));
            jsonResponse.put("GAME_OVER", gameResult.contains("GAME_OVER"));

            String responseText = jsonResponse.toString();
            
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, responseText.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(responseText.getBytes());
            os.close();
        } catch (Exception e){
            e.printStackTrace();
            return;
        }
    }
}