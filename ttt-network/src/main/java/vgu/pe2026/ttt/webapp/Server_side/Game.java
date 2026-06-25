package vgu.pe2026.ttt.webapp.Server_side;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Game {
    private Board gameBoard;

    public Game() {
        this.gameBoard = new Board(); 
    }

    public String start(int humanId, String boardState, int userMove) {
        
        StringBuilder response = new StringBuilder();

        // reading sequence: clientIdStr - boardstate - userMove - clientSignature - clientToken - receivedTTL

        // turn the string of boardstate to a 2D board
        gameBoard.deserialize(boardState);

        if (!gameBoard.isCellValid(userMove)) {
            response.append("INVALID_MOVE");
        }

        gameBoard.updateCell(userMove, humanId);

        if (gameBoard.checkWin(humanId)) {

            response.append("Player#").append(humanId).append(" won!\n");
            response.append("GAME_OVER").append("\n");

        } else if (gameBoard.findEmptyCell() == -1) {

            response.append("It is a draw!\n");
            response.append("GAME_OVER").append("\n");

        } else {

            int botId = (humanId == 1) ? 2 : 1;
            BotPlayer bot = new BotPlayer(botId); 
            bot.makeMove(gameBoard);

            if (bot.isWin(gameBoard)) {

                response.append("Player#").append(botId).append(" won!\n");
                response.append("GAME_OVER").append("\n");
                
            } else if (gameBoard.findEmptyCell() == -1) {
                response.append("It is a draw!").append("\n");
                response.append("GAMEOVER").append("\n");

            } else {

                // response.append(gameBoard.toString()).append("\n");
                response.append("Bot has moved.").append("\n");

            }
        }

        // STATE
        response.append(gameBoard.serialize());

        return response.toString();
    }
}
