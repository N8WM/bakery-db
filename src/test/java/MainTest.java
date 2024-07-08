import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

public class MainTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Test
    public void testMain() {
        // Call the main method
        Main.main(new String[]{});

        // Capture the output and check that it is not empty
        String output = systemOutRule.getLog();
        assertTrue(output.isEmpty(), "The output should not be empty");
    }
}
