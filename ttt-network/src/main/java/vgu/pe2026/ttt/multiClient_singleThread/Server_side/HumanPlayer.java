package vgu.pe2026.ttt.multiClient_singleThread.Server_side;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
// import java.util.Scanner;

public class HumanPlayer extends Player{
    
    private boolean quitFlag = false;
    // private Scanner sc;

    private BufferedReader in;
    private PrintWriter out;

    public HumanPlayer(int turn, BufferedReader in, PrintWriter out) {
        super(turn);
        this.turn = turn;
        this.quitFlag = false;
        this.in = in;
        this.out = out;
    }

    public boolean hasQuit() {       
        return this.quitFlag;
    }

    @Override
    public void makeMove(Board gameBoard){
        
        boolean valid = false;
        while (!valid) {
            out.println("TURN:" + this.turn);

            try {
                String input = in.readLine(); 
                
                if (input == null || input.equals("q")) {
                    out.println("End of the game");
                    out.println("GAME_OVER"); 
                    this.quitFlag = true;
                    valid = true;
                } else {
                    try {
                        int pos = Integer.parseInt(input);
                        if (pos >= 1 && pos <= 9) {
                            if (gameBoard.isCellValid(pos)) {
                                gameBoard.updateCell(pos, this.turn);
                                valid = true;
                            } else {
                                out.println("The cell is occupied!");
                                out.println("Player#" + this.turn + "'s turn"); 
                            }
                        } else {
                            out.println("Please, input a valid number [1-9]");
                            out.println("Player#" + this.turn + "'s turn"); 
                        }
                    } catch (NumberFormatException e) {
                        out.println("Please, input a valid number [1-9]");
                        out.println("Player#" + this.turn + "'s turn"); 
                    }
                }
            } catch (IOException e) {
                System.out.println("Error. Disconnected with Client.");
                break;
            }
        }
    }
}
