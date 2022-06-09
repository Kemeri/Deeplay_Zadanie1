import org.junit.Test;
import static org.junit.Assert.*;
public class SolutionTest {
    @Test
    public void getResult() throws Exception {
        String race = "Human";
        String field = "STWSWTPPTPTTPWPP";
        assertEquals(Solution.getResult(field, race), 10);
        race = "Swamper";
        assertEquals(Solution.getResult(field, race), 15);
        race = "Woodman";
        assertEquals(Solution.getResult(field, race), 12);
    }
}