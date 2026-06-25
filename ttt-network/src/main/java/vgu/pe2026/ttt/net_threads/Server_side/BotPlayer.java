package vgu.pe2026.ttt.net_threads.Server_side;

public class BotPlayer extends Player {
    
    public BotPlayer(int turn){ super(turn); }

    @Override
    public void makeMove(Board gameBoard){
        System.out.println("Player#" + this.turn + "'s turn");
        int pos = gameBoard.findEmptyCell();

        if(pos != -1 && gameBoard.isCellValid(pos)){
            gameBoard.updateCell(pos, this.turn);
        }
    }
}
