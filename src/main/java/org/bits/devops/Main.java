package org.bits.devops;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class Main {
    private static HttpServer server;
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws IOException {
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/convert", new EpochHandler());
        server.start();
        LOGGER.info("Service URL : http://localhost:8000/convert?epochTime=");

    }

    public static LocalDateTime convertEpochToDateTime(long epochTime) {
        Instant instant = Instant.ofEpochSecond(epochTime);
        return LocalDateTime.ofInstant(instant, ZoneOffset.ofHoursMinutes(5, 30));
    }


    static class EpochHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, List<String>> params = parseQueryParams(exchange.getRequestURI().getQuery());

            if (params.containsKey("epochTime")) {

                long epochTime = Long.parseLong(params.get("epochTime").get(0));
                var dateTime = convertEpochToDateTime(epochTime);
                LOGGER.info("Converted provided epoch time : " + epochTime + " to Date Time Format : " + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                String htmlResponse = "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "    <title>Epoch Time Converter</title>\n" +
                        "    <style>\n" +
                        "        body {\n" +
                        "            font-family: Arial, sans-serif;\n" +
                        "            background-color: #f4f4f4;\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "        }\n" +
                        "        .container {\n" +
                        "            max-width: 800px;\n" +
                        "            margin: 50px auto;\n" +
                        "            padding: 20px;\n" +
                        "            background-color: #fff;\n" +
                        "            border-radius: 10px;\n" +
                        "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
                        "            display: flex;\n" +
                        "            justify-content: space-between;\n" +
                        "        }\n" +
                        "        .card {\n" +
                        "            padding: 20px;\n" +
                        "            background-color: #f9f9f9;\n" +
                        "            border-radius: 5px;\n" +
                        "            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
                        "            text-align: center;\n" +
                        "        }\n" +
                        "        h2 {\n" +
                        "            color: #333;\n" +
                        "        }\n" +
                        "        p {\n" +
                        "            color: #666;\n" +
                        "        }\n" +
                        "    </style>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <div class=\"container\">\n" +
                        "        <div class=\"card\">\n" +
                        "            <h2>Year</h2>\n" +
                        "            <p>" + dateTime.getYear() + "</p>\n" +
                        "        </div>\n" +
                        "        <div class=\"card\">\n" +
                        "            <h2>Month</h2>\n" +
                        "            <p>" + dateTime.getMonthValue() + "</p>\n" +
                        "        </div>\n" +
                        "        <div class=\"card\">\n" +
                        "            <h2>Day</h2>\n" +
                        "            <p>" + dateTime.getDayOfMonth() + "</p>\n" +
                        "        </div>\n" +
                        "        <div class=\"card\">\n" +
                        "            <h2>Hours</h2>\n" +
                        "            <p>" + dateTime.getHour() + "</p>\n" +
                        "        </div>\n" +
                        "        <div class=\"card\">\n" +
                        "            <h2>Minutes</h2>\n" +
                        "            <p>" + dateTime.getMinute() + "</p>\n" +
                        "        </div>\n" +
                        "        <div class=\"card\">\n" +
                        "            <h2>Seconds</h2>\n" +
                        "            <p>" + dateTime.getSecond() + "</p>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "</body>\n" +
                        "</html>";



                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, htmlResponse.getBytes().length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(htmlResponse.getBytes());
                }
            } else {

                exchange.sendResponseHeaders(405, -1);
            }
        }

        private Map<String, List<String>> parseQueryParams(String query) {
            return Arrays
                    .stream(query.split("&"))
                    .map(p -> p.split("="))
                    .collect(java.util.stream.Collectors.groupingBy(p -> p[0], java.util.stream.Collectors.mapping(p -> p[1], java.util.stream.Collectors.toList())));
        }
    }

    public static void stopServer(){
        server.stop(1);
    }
}