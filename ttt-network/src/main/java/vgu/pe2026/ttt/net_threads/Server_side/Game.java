package vgu.pe2026.ttt.net_threads.Server_side;

import java.lang.Thread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Game {
    private Socket clientSocket;
    private Board gameBoard;
    private Player player1;
    private Player player2;
    private int currentTurn;
    private boolean isEnd;

    public Game(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.gameBoard = new Board(); 
        this.currentTurn = 1;
        this.isEnd = false;
    }

    public void start() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); 

            String clientIdStr = in.readLine();
            int humanId = Integer.parseInt(clientIdStr);

            if (humanId == 1) {
                player1 = new HumanPlayer(1, in, out);
                player2 = new BotPlayer(2);
            } else {
                player1 = new BotPlayer(1);
                player2 = new HumanPlayer(2, in, out);
            }

            out.println("Hello!"); 

            while (!isEnd) {
                gameBoard.display(out);

                Player currentPlayer = (currentTurn == player1.getTurn()) ? this.player1 : this.player2;
                
                out.println("Player#" + currentPlayer.getTurn() + "'s turn");
                
                currentPlayer.makeMove(gameBoard);

                if (currentPlayer.hasQuit()) {
                    isEnd = true;
                    out.println("GAME_OVER");
                } else {
                    if (currentPlayer.isWin(gameBoard)) {
                        gameBoard.display(out);
                        out.println("Player#" + currentPlayer.getTurn() + " won!"); 
                        out.println("GAME_OVER");
                        isEnd = true;
                    }

                    if (!isEnd && gameBoard.findEmptyCell() == -1) {
                        gameBoard.display(out);
                        out.println("It is a draw!"); 
                        out.println("GAME_OVER");
                        isEnd = true;
                    }

                    if (!isEnd) {
                        currentTurn = (currentTurn == 1) ? 2 : 1;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("End session with Client.");
        }
    }
}
