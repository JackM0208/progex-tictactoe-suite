package vgu.pe2026.ttt.basic;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BotPlayerTest {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private final String ls = System.lineSeparator();

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
    public void computerMoveStrategyTest(){

        Board testBoard = new Board(System.out);
        BotPlayer testBotPlayer = new BotPlayer(2);

        testBotPlayer.makeMove(testBoard);
        assertEquals(2, testBoard.getCellValue(1));

        testBoard.updateCell(2, 1);
        testBoard.updateCell(3, 1);

        testBotPlayer.makeMove(testBoard);
        assertEquals(2, testBoard.getCellValue(4));

        testBoard.updateCell(5, 1);
        testBoard.updateCell(6, 1);
        testBoard.updateCell(7, 1);
        testBoard.updateCell(8, 1);

        testBotPlayer.makeMove(testBoard);
        assertEquals(2, testBoard.getCellValue(9));
    }
}
