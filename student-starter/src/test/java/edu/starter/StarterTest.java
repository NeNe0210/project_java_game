package edu.starter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class StarterTest {
 @Test void clampingIsCorrect(){assertEquals(10,Checks.within(12,0,10));}
 @Test void missingNumberIsRejected(){assertEquals(-1,ConsoleChoice.read(new java.util.Scanner("abc\n"),1,4));}
}
