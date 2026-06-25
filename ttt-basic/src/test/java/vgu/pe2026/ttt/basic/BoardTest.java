package vgu.pe2026.ttt.basic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BoardTest {
    private final String ls = System.lineSeparator();


    @Test
    public void testDisplay(){

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printer = new PrintStream(out);

        Board testBoard = new Board(printer);
        testBoard.display();

        String expectedOutput = 
        "| 0 | 0 | 0 |" + ls +
        "| 0 | 0 | 0 |" + ls +  
        "| 0 | 0 | 0 |" + ls;

        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testIsCellVaidPostive() {
        Board testBoard = new Board(System.out);

        assertTrue( testBoard.isCellValid(9) );
    }
    
    @Test
    public void testIsCellVaidNegative() {
        Board testBoard = new Board(System.out);
        int[] state = { 
            0, 0, 0, 
            0, 0, 0, 
            0, 1, 0
        };

        populateBoard(testBoard, state);

        assertFalse( testBoard.isCellValid(8) );
    }

    @Test
    public void testUpdateCell() {
        Board testBoard = new Board(System.out);

        int pos = 8;
        int playerTurn = 1;

        testBoard.updateCell(pos, playerTurn);

        assertEquals(1, testBoard.getCellValue(8));
    }

    @Test
    public void testFindEmptyCell() {
        Board testBoard = new Board(System.out);
        int[] state = {
            1, 2, 0,
            0, 0, 1,
            1, 1, 2
        };

        populateBoard(testBoard, state);
        assertEquals(3, testBoard.findEmptyCell());
    }

    @Test
    public void testCheckWinHorizontal(){

        Board testBoard = new Board(System.out);
        int[] state = {
            1, 1, 1,
            0, 2, 2,
            0, 0, 0
        };

        int turn = 1;

        populateBoard(testBoard, state);
        assertTrue(testBoard.checkWin(turn));
    }

    @Test
    public void testCheckWinVertical(){

        Board testBoard = new Board(System.out);
        int[] state = {
            1, 1, 0,
            1, 2, 2,
            1, 2, 0
        };

        int turn = 1;

        populateBoard(testBoard, state);
        assertTrue(testBoard.checkWin(turn));
    }

    @Test
    public void testCheckWinDiagonal(){

        Board testBoard = new Board(System.out);
        int[] state = {
            1, 1, 2,
            0, 2, 2,
            2, 1, 0
        };

        int turn = 2;

        populateBoard(testBoard, state);
        assertTrue(testBoard.checkWin(turn));
    }

    @Test
    public void testCheckWinNegative(){

        Board testBoard = new Board(System.out);
        int[] state = {
            0, 0, 1,
            2, 2, 1,
            1, 2, 0
        };

        populateBoard(testBoard, state);

        assertFalse(testBoard.checkWin(2));
        assertFalse(testBoard.checkWin(1));
    }

    private void populateBoard(Board board, int[] array){
        for(int i = 0; i < array.length; i++){
            if(array[i] != 0){
                board.updateCell(i + 1, array[i]);
            }
        }
    }
}

    
