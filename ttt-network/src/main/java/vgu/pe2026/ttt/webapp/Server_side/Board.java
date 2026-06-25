package vgu.pe2026.ttt.webapp.Server_side;

import java.io.PrintWriter;

public class Board {
    private int[][] matrix;
    private int w = 3;

    public Board(){
        this.matrix = new int[w][w];
        this.init(); 
    }

    public void init(){
        for(int i = 0; i < w; i++){
            for(int j = 0; j < w; j++){
                matrix[i][j] = 0;
            }
        }
    }

    public void display(PrintWriter printer) {
        for (int i = 0; i < w; i++) {
            printer.print("|"); 
            for (int j = 0; j < w; j++) {
                printer.print(" " + matrix[i][j] + " |");
            }
            printer.println();
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
        isWin = true; 
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

    // Chuyển mảng 2D thành chuỗi dạng "000 100 000"
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                sb.append(matrix[i][j]);
            }
            if (i < w - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    // Nạp dữ liệu từ chuỗi dạng "000 100 000" vào mảng 2D
    public void deserialize(String str) {
        String[] rows = str.split(" ");
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                matrix[i][j] = Character.getNumericValue(rows[i].charAt(j));
            }
        }
    }
}