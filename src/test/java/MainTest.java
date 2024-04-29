import org.bits.devops.Main;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainTest {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void testConvertEpochToDateTime() {
        long epochTime = 1616634000; // Example epoch time
        LocalDateTime expectedDateTime = LocalDateTime.parse("2021-03-25 06:30:00", formatter); // Expected date and time

        LocalDateTime actualDateTime = Main.convertEpochToDateTime(epochTime);
        assertEquals(expectedDateTime.getHour(), actualDateTime.getHour());
        assertEquals(expectedDateTime.getMinute(), actualDateTime.getMinute());
        assertEquals(expectedDateTime.getYear(), actualDateTime.getYear());
        assertEquals(expectedDateTime.getMonth(), actualDateTime.getMonth());
    }

    @Test
    public void testConvertEpochToDateTimeNegative() {
        long epochTime = -1; // Negative epoch time
        LocalDateTime expectedDateTime = LocalDateTime.parse("1970-01-01 05:29:59", formatter); // Expected date and time for negative epoch time

        LocalDateTime actualDateTime = Main.convertEpochToDateTime(epochTime);
        assertEquals(expectedDateTime.getMonth(), actualDateTime.getMonth());
        assertEquals(expectedDateTime.getHour(), actualDateTime.getHour());
        assertEquals(expectedDateTime.getSecond(), actualDateTime.getSecond());
    }

    @Test
    public void testEndpointWithResponse() throws URISyntaxException, IOException, InterruptedException {
        Main.main(null);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8000/convert?epochTime=1714417738"))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 200);
        String resBody = new String(response.body().getBytes(StandardCharsets.UTF_8));
        assertTrue(resBody.contains("""
                   <div class="card">
                            <h2>Minutes</h2>
                            <p>38</p>
                        </div>
                """));
        Main.stopServer();
    }

    @Test
    public void testEndpointWithNoResponse() throws URISyntaxException, IOException, InterruptedException {
        Main.main(null);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8000/convert?epoch=1714417738"))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.statusCode(), 405);

        Main.stopServer();
    }
}
