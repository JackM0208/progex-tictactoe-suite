package vgu.pe2026.ttt.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HumanPlayerTest {
    private final String ls = System.lineSeparator();
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(out, true));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void makeMoveTest(){

        Board testBoard = new Board(System.out);

        String simulatedInput = "5" + ls;
        
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner testSc = new Scanner(in);

        HumanPlayer testPlayer = new HumanPlayer(1, testSc);
        testPlayer.makeMove(testBoard);

        assertEquals(1, testBoard.getCellValue(5));
    }

    

    @Test
    public void testNonIntegerInput(){
        Board testBoard = new Board(System.out);

        String simulatedInput = "x" + ls + "5" + ls;

        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner testSc = new Scanner(in);

        HumanPlayer testPlayer = new HumanPlayer(1, testSc);
        testPlayer.makeMove(testBoard);

        String expectedConversation = 
            "Player#1's turn" + ls +
            "Please, input a valid number [1-9]" + ls + 
            "Player#1's turn" + ls; 

        assertEquals(expectedConversation, out.toString());
    }

    @Test
    public void testIndexOutOfRange(){
        Board testBoard = new Board(System.out);

        String simulatedInput = "0\n-1\n10\n3";

        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner testSc = new Scanner(in);

        HumanPlayer testPlayer = new HumanPlayer(1, testSc);
        testPlayer.makeMove(testBoard);

        String expectedConversation = 
            "Player#1's turn" + ls +
            "Please, input a valid number [1-9]" + ls + 

            "Player#1's turn" + ls +
            "Please, input a valid number [1-9]" + ls + 

            "Player#1's turn" + ls +
            "Please, input a valid number [1-9]" + ls + 

            "Player#1's turn" + ls; 

        assertEquals(expectedConversation, out.toString());
    }

    @Test
    public void testOccupiedCellInput(){
        Board testBoard = new Board(System.out);
        testBoard.updateCell(1, 1);

        String simulatedInput = "1\n2\n";

        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner testSc = new Scanner(in);

        HumanPlayer testPlayer = new HumanPlayer(2, testSc);
        testPlayer.makeMove(testBoard);

        String expectedConversation = 
            "Player#2's turn" + ls +
            "The cell is occupied!" + ls +
            "Player#2's turn" + ls;

        assertEquals(expectedConversation, out.toString());
    }
}
