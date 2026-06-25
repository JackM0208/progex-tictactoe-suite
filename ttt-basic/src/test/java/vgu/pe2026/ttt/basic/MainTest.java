package vgu.pe2026.ttt.basic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.io.InputStream;


public class MainTest 
{
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
    public void testNoInputArgument()
    {
        App.main(new String[]{});
        assertEquals("Please, input a valid option [1-2]", out.toString());
    }

    @Test
    public void testInvalidArguments_abc()
    {
        App.main(new String[]{"abc"});
        assertEquals("Please, input a valid option [1-2]", out.toString());
    }

    @Test
    public void testInvalidArguments_0()
    {
        App.main(new String[]{"0"});
        assertEquals("Please, input a valid option [1-2]", out.toString());
    }

    @Test
    public void testInvalidArguments_minus1()
    {
        App.main(new String[]{"-1"});
        assertEquals("Please, input a valid option [1-2]", out.toString());
    }

    // TS-005
    @Test
    public void testExtraArguments()
    {
        App.main(new String[]{"1 2"});
        assertEquals("Please, input a valid option [1-2]", out.toString());
    }

    // Initial Board Mapping
    @Test 
    public void testInitialBoardMapping()
    {
        String script = "5" + ls + "q" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"1"});

        String expectedInitialBoard = 
        "| 0 | 0 | 0 |" + ls + 
        "| 0 | 0 | 0 |" + ls + 
        "| 0 | 0 | 0 |";

        assertTrue(out.toString().contains(expectedInitialBoard));

        String boardAfterMove1 =
        "| 0 | 0 | 0 |" + ls + 
        "| 0 | 1 | 0 |" + ls + 
        "| 0 | 0 | 0 |";

        assertTrue(out.toString().contains(boardAfterMove1));

        String boardAfterMove2 =
        "| 2 | 0 | 0 |" + ls + 
        "| 0 | 1 | 0 |" + ls + 
        "| 0 | 0 | 0 |";

        assertTrue(out.toString().contains(boardAfterMove2));
    }

    // TS-008
    @Test
    public void HandleNonIntegerInput() 
    {
        String script = "abc" + ls + "@" + ls + ls + "q" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"1"});

        String expectedOutput = 
        "Hello!" + ls +
        "| 0 | 0 | 0 |" + ls +
        "| 0 | 0 | 0 |" + ls + 
        "| 0 | 0 | 0 |" + ls +
        "Player#1's turn" + ls + 

        "Please, input a valid number [1-9]" + ls +
        "Player#1's turn" + ls + 

        "Please, input a valid number [1-9]" + ls +
        "Player#1's turn" + ls + 

        "Please, input a valid number [1-9]" + ls +
        "Player#1's turn" + ls + 

        "End of the game" + ls;

        assertEquals(expectedOutput, out.toString());
    }

    // TS-009
    @Test
    public void testStartupMessageAndOrder() 
    {
        String script = "q" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"1"});

        String expectedOutput = 
        "Hello!" + ls +
        "| 0 | 0 | 0 |" + ls +
        "| 0 | 0 | 0 |" + ls + 
        "| 0 | 0 | 0 |" + ls +
        "Player#1's turn" + ls + 
        "End of the game" + ls;

        assertEquals(expectedOutput, out.toString());
    }

    // TS-010
    @Test
    public void verifyQCaseSensitivity() 
    {
        String script = "Q" + ls + " q" + ls + "q " + ls + "q" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"1"});

        String expectedOutput = 
        "Hello!" + ls +
        "| 0 | 0 | 0 |" + ls +
        "| 0 | 0 | 0 |" + ls + 
        "| 0 | 0 | 0 |" + ls +
        "Player#1's turn" + ls + 

        "Please, input a valid number [1-9]" + ls +
        "Player#1's turn" + ls + 

        "Please, input a valid number [1-9]" + ls +
        "Player#1's turn" + ls + 

        "Please, input a valid number [1-9]" + ls +
        "Player#1's turn" + ls + 

        "End of the game" + ls;

        assertEquals(expectedOutput, out.toString());
    }

    // TS-011
    @Test
    public void rejectIntegerOutside1_9() 
    {
        String script = "0" + ls + "10" + ls + "-3" + ls + "q" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"1"});

        String expectedOutput = 
        "Hello!" + ls +
        "| 0 | 0 | 0 |" + ls +
        "| 0 | 0 | 0 |" + ls + 
        "| 0 | 0 | 0 |" + ls +
        "Player#1's turn" + ls + 

        "Please, input a valid number [1-9]" + ls +
        "Player#1's turn" + ls + 

        "Please, input a valid number [1-9]" + ls +
        "Player#1's turn" + ls + 

        "Please, input a valid number [1-9]" + ls +
        "Player#1's turn" + ls + 

        "End of the game" + ls;

        assertEquals(expectedOutput, out.toString());
    }

    // TS-012
    @Test
    public void rejectMoveToOccupiedCell() 
    {
        String script = "1" + ls + "1" + ls + "q" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"1"});

        String expectedOutput = 
        "Hello!" + ls +
        "| 0 | 0 | 0 |" + ls +
        "| 0 | 0 | 0 |" + ls + 
        "| 0 | 0 | 0 |" + ls +
        "Player#1's turn" + ls + 

        "| 1 | 0 | 0 |" + ls +
        "| 0 | 0 | 0 |" + ls + 
        "| 0 | 0 | 0 |" + ls +
        "Player#2's turn" + ls + 

        "| 1 | 2 | 0 |" + ls +
        "| 0 | 0 | 0 |" + ls + 
        "| 0 | 0 | 0 |" + ls +
        "Player#1's turn" + ls + 

        "The cell is occupied!" + ls + 
        "Player#1's turn" + ls + 

        "End of the game" + ls;

        assertEquals(expectedOutput, out.toString());
    }

    // TS-013-Column
    @Test
    public void testHumanWin_Column() {
        String script = "1" + ls + "4" + ls + "7" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"1"});
        String history = out.toString();

        assertTrue(history.contains("Player#1 won!"));
    }

    // TS-013-Row
    @Test
    public void testHumanWin_Row() {
        String script = "5" + ls + "4" + ls + "3" + ls + "6" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"2"});
        String history = out.toString();

        assertTrue(history.contains("Player#1 won!"));
    }

    // TS-013-Diagonal
    @Test
    public void testHumanWin_Diagonal() {
        String script = "1" + ls + "5" + ls + "9" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"1"});
        String history = out.toString();

        assertTrue(history.contains("Player#1 won!"));
    }

    // TS-014
    @Test
    public void testComputerWin() {
        String script = "5" + ls + "4" + ls + "7" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"2"});
        String history = out.toString();

        assertTrue(history.contains("Player#1 won!"));
    }

    // TS-015
    @Test
    public void drawDetectionAfterHumanMove() {
        String script = "5" + ls + "2" + ls + "7" + ls + "6" + ls + "9" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"1"});
        String history = out.toString();

        assertTrue(history.contains("It is a draw!"));
    }

    // TS-016
    @Test
    public void drawDetectionAfterComputerMove() {
        String script = "2" + ls + "5" + ls + "7" + ls + "9";
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"2"});
        String history = out.toString();

        assertTrue(history.contains("It is a draw!"));
    }

// TS-017
    @Test
    public void computerChoosesFirstAvailableCell() {
        // Kịch bản: Human đánh ô 2, sau đó chốt sổ bằng q.
        String script = "2" + ls + "q" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[]{"2"});

        String expectedFlow = 
            "Hello!" + ls +
            "| 0 | 0 | 0 |" + ls +
            "| 0 | 0 | 0 |" + ls +
            "| 0 | 0 | 0 |" + ls +
            "Player#1's turn" + ls + 
            "| 1 | 0 | 0 |" + ls +   
            "| 0 | 0 | 0 |" + ls +
            "| 0 | 0 | 0 |" + ls +
            "Player#2's turn" + ls + 
            "| 1 | 2 | 0 |" + ls +   
            "| 0 | 0 | 0 |" + ls +
            "| 0 | 0 | 0 |" + ls +
            "Player#1's turn" + ls + 
            "| 1 | 2 | 1 |" + ls +   
            "| 0 | 0 | 0 |" + ls +
            "| 0 | 0 | 0 |" + ls +
            "Player#2's turn" + ls + 
            "End of the game" + ls;

        assertEquals(expectedFlow, out.toString());
    }
}
