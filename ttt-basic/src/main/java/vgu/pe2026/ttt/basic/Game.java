package vgu.pe2026.ttt.basic;

import java.lang.Thread;

public class Game {
    private Board gameBoard;
    private Player player1;
    private Player player2;
    private int currentTurn;
    private boolean isEnd;

    public Game(Player player1, Player player2){
        this.gameBoard = new Board(System.out);
        this.player1 = player1;
        this.player2 = player2;
        this.currentTurn = 1;
        this.isEnd = false;
    }

    public void start(){

        // Thread gameThread = new Thread(
        //     ()
        //     -> {

                while(!isEnd){
                    gameBoard.display();

                    Player currentPlayer = (currentTurn == player1.getTurn()) ? this.player1 : this.player2;
                    
                    currentPlayer.makeMove(gameBoard);

                    if(currentPlayer.hasQuit()){
                        isEnd=true;
                    }
                    else{
                        if(currentPlayer.isWin(gameBoard)){
                            gameBoard.display();
                            System.out.println("Player#" + currentPlayer.getTurn() + " won!");
                            isEnd = true;
                        }

                        if (!isEnd && gameBoard.findEmptyCell() == -1){
                            gameBoard.display();
                            System.out.println("It is a draw!");
                            isEnd = true;
                        }

                        if (!isEnd){
                            currentTurn = (currentTurn == 1) ? 2 : 1;
                        }
                    }
                }
            // }
        // );

        // gameThread.start();
        
    }
}
