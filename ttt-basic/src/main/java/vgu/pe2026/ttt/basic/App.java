package vgu.pe2026.ttt.basic;

import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {   
        int humanId;

        if (args.length == 0){
            System.out.print("Please, input a valid option [1-2]");
            return;
        } 
        else if (args.length != 1){
            System.out.print("Please, input a valid option [1-2]");
            return;
        }
        else {
            try {
                humanId = Integer.parseInt(args[0]);
                if (humanId != 1 && humanId != 2){
                    System.out.print("Please, input a valid option [1-2]");
                    return;
                } else {
                    System.out.print("Hello!" + System.lineSeparator());
                }
            } catch (NumberFormatException e) {
                System.out.print("Please, input a valid option [1-2]");
                return;
            }
        }  

        int botId = (humanId == 1) ? 2 : 1;
        Scanner sc = new Scanner(System.in);

        Player you = new HumanPlayer(humanId, sc);
        Player bot = new BotPlayer(botId);

        Game tictactoe = new Game(you, bot);
        tictactoe.start();         
    }
}