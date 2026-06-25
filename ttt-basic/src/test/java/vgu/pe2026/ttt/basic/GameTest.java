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

public class GameTest {
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

    // Turn handoff symmetry
    @Test 
    public void turnHandOffSymmetryTest_HumanFirst(){
        String script = "5" + ls + "q" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[] {"1"});

        String expectedFlow = 
            "Hello!" + ls + 

            "| 0 | 0 | 0 |" + ls +
            "| 0 | 0 | 0 |" + ls + 
            "| 0 | 0 | 0 |" + ls + 

            "Player#1's turn" + ls +
            "| 0 | 0 | 0 |" + ls +
            "| 0 | 1 | 0 |" + ls + 
            "| 0 | 0 | 0 |" + ls + 
            "Player#2's turn" + ls +

            "| 2 | 0 | 0 |" + ls +
            "| 0 | 1 | 0 |" + ls + 
            "| 0 | 0 | 0 |" + ls + 
            "Player#1's turn" + ls + 
            "End of the game" + ls;

        assertEquals(expectedFlow, out.toString());
    }

    @Test
    public void turnHandOffSymmetryTest_BotFirst(){
        String script = "q" + ls;
        System.setIn(new ByteArrayInputStream(script.getBytes()));

        App.main(new String[] {"2"});

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
            "End of the game" + ls;

        assertEquals(expectedFlow, out.toString());
    }
}
