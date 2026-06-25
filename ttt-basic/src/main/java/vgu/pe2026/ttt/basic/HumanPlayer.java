package vgu.pe2026.ttt.basic;

import java.util.Scanner;

public class HumanPlayer extends Player{
    
    private boolean quitFlag = false;
    private Scanner sc;

    public HumanPlayer(int turn, Scanner sc){
        super(turn);
        this.sc = sc;
    }

    public boolean hasQuit() {       
        return this.quitFlag;
    }

    @Override
    public void makeMove(Board gameBoard){
        
        boolean valid = false;

        while(!valid){
            System.out.print("Player#" + this.turn + "'s turn" + System.lineSeparator());
            String input = sc.nextLine(); 

            if (input.equals("q")) {
                System.out.println("End of the game");
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
                            System.out.print("The cell is occupied!" + System.lineSeparator());
                        }
                    } else {
                        System.out.print("Please, input a valid number [1-9]" + System.lineSeparator());
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Please, input a valid number [1-9]" + System.lineSeparator());
                }
            }
        }            
        
    }
}
