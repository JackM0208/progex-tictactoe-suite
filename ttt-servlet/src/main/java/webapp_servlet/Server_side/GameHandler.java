package vgu.pe2026.ttt.webapp_servlet.Server_side;

// import com.sun.net.httpserver.HttpHandler;
// import com.sun.net.httpserver.HttpExchange;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.PrintStream;
import java.io.BufferedReader;
import org.json.JSONObject;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.io.PrintWriter;

@WebServlet("/play")
public class GameHandler extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setStatus(200);
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        String gameResult = "";

        String requestBody = req.getReader().lines().collect(Collectors.joining("\n"));
        JSONObject jsonRequest = new JSONObject(requestBody);

        System.out.println("Received: " + jsonRequest.toString());

        int id = jsonRequest.getInt("ID");
        String boardState = jsonRequest.getString("STATE");
        int move = jsonRequest.getInt("MOVE");

        Game tictactoe = new Game();
        gameResult = tictactoe.start(id, boardState, move);

        System.out.println("Response from Game: " + gameResult);

        String parts[] = gameResult.split("\n");

        System.out.println("Parts length: " + parts.length);;
        for(int i = 0; i < parts.length; i++){
            System.out.println("parts[" + i + "] = " + parts[i]);
        }

        JSONObject jsonResponse = new JSONObject();

        jsonResponse.put("MESSAGE", parts[0]);
        jsonResponse.put("STATE", parts[parts.length - 1]);
        jsonResponse.put("GAME_OVER", gameResult.contains("GAME_OVER"));

        String responseText = jsonResponse.toString();
        
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        
        // just debugging
        System.out.println("=== JSON RESPONSE BEING SENT ===");
        System.out.println(responseText);
        System.out.println("================================");

        out.print(responseText);
        out.flush();
        out.close();
    }

    @Override
    public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.setStatus(204);
    }
}
