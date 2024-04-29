import org.bits.devops.Main;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void testConvertEpochToDateTime() {
        long epochTime = 1616634000; // Example epoch time
        String expectedDateTime = "2021-03-25 06:30:00"; // Expected date and time

        String actualDateTime = Main.convertEpochToDateTime(epochTime);
        assertEquals(expectedDateTime, actualDateTime);
    }

    @Test
    public void testConvertEpochToDateTimeNegative() {
        long epochTime = -1; // Negative epoch time
        String expectedDateTime = "1970-01-01 05:29:59"; // Expected date and time for negative epoch time

        String actualDateTime = Main.convertEpochToDateTime(epochTime);
        assertEquals(expectedDateTime, actualDateTime);
    }
}
