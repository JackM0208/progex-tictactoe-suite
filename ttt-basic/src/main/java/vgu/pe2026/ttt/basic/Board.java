package vgu.pe2026.ttt.basic;

import java.io.PrintStream;

public class Board {
    private int[][] matrix;
    private int w = 3;
    private PrintStream printer;

    public Board(PrintStream printer){
        this.printer = printer;
        this.matrix = new int[w][w];
    }

    public void init(){
        for(int i = 0; i < w; i++){
            for(int j = 0; j < w; j++){
                matrix[i][j] = 0;
            }
        }
    }

    public void display() {
    for (int i = 0; i < w; i++) {
        this.printer.print("|"); 
        for (int j = 0; j < w; j++) {
            this.printer.print(" " + matrix[i][j] + " |");
        }
        this.printer.println();
        }
    }

    public void updateCell(int pos, int turn){
        pos -= 1;
        int row = pos / this.w;
        int col = pos % this.w;

        this.matrix[row][col] = turn;   
    }

    public int findEmptyCell(){
        for(int i = 0; i < this.w; i++){
            for(int j = 0; j < this.w; j++){
                if (matrix[i][j] == 0){
                    int pos = (i * this.w) + j + 1;
                    return pos;
                }
            }
        }
        return -1;
    }

    public boolean isCellValid(int pos){
        if (pos < 1 || pos > this.w * this.w){
            return false;
        }

        pos -= 1;
        int row = pos / this.w;
        int col = pos % this.w;

        if (matrix[row][col] == 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean checkWin(int turn){
        
        boolean isWin = true;

        for (int i = 0; i < this.w; i++){
            isWin = true;
            for(int j = 0; j < this.w && isWin; j++){
                if(this.matrix[i][j] != turn){
                    isWin = false;
                } 
            }
            if (isWin) return true;
        }   

        // check for columns 

        for (int i = 0; i < this.w; i++){
            isWin = true;
            for(int j = 0; j < this.w && isWin; j++){
                if(this.matrix[j][i] != turn){
                    isWin = false;
                } 
            }
            if (isWin) return true;
        }  

        // check for diagonals
        /* 00    02
              11
           20    22 */

        isWin = true; // the case where after checking for columns, none passes, so isWin is still False
        for (int i = 0; i < this.w && isWin; i++){
            if (this.matrix[i][i] != turn){
                isWin = false;
            }
        }
        if (isWin) return true;

        isWin = true;
        for (int i = 0; i < this.w && isWin; i++){
            if (this.matrix[i][(this.w - 1) - i] != turn){
                isWin = false;
            }
        }

        return isWin;
    }

    public int getWidth(){
        return this.w;
    }

    public int getCellValue(int pos) {
        if (pos < 1 || pos > this.w * this.w) return -1; 
        
        int index = pos - 1;
        int row = index / this.w;
        int col = index % this.w;
        
        return this.matrix[row][col];
    }
}

/* 
pos = (row * w) + col + 1
pos - 1 = (row * w) + col
(pos - 1)/ w = row + col/w 
but col/w is always < 1 and is a float, so row = (pos-1)/w
col = pos - 1 - (row * w)

A \mod B = A - (B * \lfloor A/B \rfloor)
A is (pos - 1)
B is w
row is \lfloor A/B \rfloor
*/
