import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class WeatherHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] weatherConditions = {"sunny", "cloudy", "rainy", "snowy"};
        Random random = new Random();
        int index = random.nextInt(weatherConditions.length);
        String response = "The weather is " + weatherConditions[index];
        exchange.sendResponseHeaders(200, response.length());
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
