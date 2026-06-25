package vgu.pe2026.ttt.multiClient_singleThread.Server_side;

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
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        )        
        {
            DatabaseUtils.cleanExpiredTokens();
            String clientIdStr = in.readLine();
            int humanId = Integer.parseInt(clientIdStr);

            if (humanId == 1) {
                player1 = new HumanPlayer(1, in, out);
                player2 = new BotPlayer(2);
            } else {
                player1 = new BotPlayer(1);
                player2 = new HumanPlayer(2, in, out);
            }

            Player human = (humanId == 1) ? player1 : player2;
            Player bot = (humanId == 1) ? player2 : player1;

            // reading sequence: clientIdStr - boardstate - userMove - clientSignature - clientToken - receivedTTL
            String boardState = in.readLine();

            // what server does after the initial connection
            if (boardState.equals("START")) {
                initialize(out);
                return;
            }

            String userMoveStr = in.readLine(); 
            int userMove = Integer.parseInt(userMoveStr);

            String clientSignature = in.readLine();
            String clientToken = in.readLine();

            String receivedTTL = in.readLine();
            long clientTTL = Long.parseLong(receivedTTL);

            // -- this is all the security checks every time server receives a ticket --

            if(DatabaseUtils.isTokenUsed(clientToken)){
                out.println("WARNING: Token already used! Replay attack detected.");
                out.println("GAME_OVER");
                return;
            }

            if(!checkBoardSignature(boardState, clientSignature)){
                out.println("WARNING: Board state tampering detected!");
                out.println("GAME_OVER");
                return;
            }

            if(!checkToken(boardState, clientToken, clientTTL)){
                out.println("WARNING: Client sends the wrong token!");
                out.println("GAME_OVER");
                return;
            }

            if(!checkTimeOut(clientTTL)){
                out.println("WARNING: Client's move takes more than 10 seconds");
                out.println("GAME_OVER");
                return;
            }

            // -- if it reaches here, means that client is playing honestly (so far) -- 

            // rip the ticket
            DatabaseUtils.saveUsedToken(clientToken, clientTTL);

            // turn the string of boardstate to a 2D board
            gameBoard.deserialize(boardState);

            if (!gameBoard.isCellValid(userMove)) {
                out.println("INVALID_MOVE");
                return; 
            }

            gameBoard.updateCell(userMove, human.getTurn());

            if (human.isWin(gameBoard)) {
                gameBoard.display(out);

                out.println("Player#" + human.getTurn() + " won!"); 
                out.println("GAME_OVER");
            } else if (gameBoard.findEmptyCell() == -1) {
                gameBoard.display(out);

                out.println("It is a draw!"); 
                out.println("GAME_OVER");
            } else {

                bot.makeMove(gameBoard);

                if (bot.isWin(gameBoard)) {
                    gameBoard.display(out);

                    out.println("Player#" + bot.getTurn() + " won!"); 
                    out.println("GAME_OVER");
                } else if (gameBoard.findEmptyCell() == -1) {
                    gameBoard.display(out);

                    out.println("It is a draw!"); 
                    out.println("GAME_OVER");
                } else {

                    gameBoard.display(out);
                    out.println("Bot has moved.");
                }
            }

            sendTicket(out);
            
        } catch (Exception e) {
            System.out.println("End session with Client.");
        }
    }

    public void initialize(PrintWriter out){
        gameBoard.display(out);

        String serializedInitialBoard = gameBoard.serialize();
        String signature = CryptographicUtils.generateSignature(serializedInitialBoard);

        long timeToLive = System.currentTimeMillis() + 10000;
        String token = CryptographicUtils.generateToken(serializedInitialBoard, timeToLive);
        
        out.println("STATE:" + serializedInitialBoard);
        out.println("SIG:" + signature);

        out.println("TOKEN:" + token);
        out.println("TTL:" + timeToLive);
    }

    public void sendTicket(PrintWriter out){
        String newBoardState = gameBoard.serialize();
        String newSignature = CryptographicUtils.generateSignature(newBoardState);

        long timeToLive = System.currentTimeMillis() + 10000;
        String token = CryptographicUtils.generateToken(newBoardState, timeToLive);
        
        out.println("STATE:" + newBoardState);
        out.println("SIG:" + newSignature);

        out.println("TOKEN:" + token);
        out.println("TTL:" + timeToLive);
    }

    public boolean checkBoardSignature(String boardState, String clientSignature){
        if (CryptographicUtils.verifySignature(boardState, clientSignature)) {
            return true; 
        }
        return false;
    }

    public boolean checkToken(String boardState, String receivedToken, long timeToLive){
        if (CryptographicUtils.verifyToken(boardState, timeToLive, receivedToken)){
            return true;
        }

        return false;
    }

    public boolean checkTimeOut(long timeToLive){
        if(System.currentTimeMillis() < timeToLive){
            return true;
        }

        return false;
    }
}



