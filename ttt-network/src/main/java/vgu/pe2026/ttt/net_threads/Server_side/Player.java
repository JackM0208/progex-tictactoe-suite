package vgu.pe2026.ttt.net_threads.Server_side;

abstract class Player {
    protected int turn;

    public Player(int turn){
        this.turn = turn;
    }

    public int getTurn(){
        return this.turn;
    }
    
    public boolean isWin(Board gameBoard){
        return gameBoard.checkWin(this.turn);
    }

    public boolean hasQuit(){
        return false;
    }

    public abstract void makeMove(Board gameBoard);
}
