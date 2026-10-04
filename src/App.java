import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class App {
    public static final List<String> QUOTES = List.of(
        "The only way to do great work is to love what you do.",
        "Stay hungry, stay foolish.",
        "Success is not final.",
        "Believe you can and you're halfway there.",
        "Simplicity is the ultimate sophistication."
    );

    public static String randomQuote() {
        return QUOTES.get(ThreadLocalRandom.current().nextInt(QUOTES.size()));
    }

    public static HttpServer start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/healthz", ex -> send(ex, 200, "OK"));
        server.createContext("/quote", ex -> send(ex, 200, randomQuote()));
        server.createContext("/", ex -> {
            if (ex.getRequestURI().getPath().equals("/")) {
                send(ex, 200, "Welcome to Random Quote API!");
            } else {
                send(ex, 404, "not found");
            }
        });
        server.start();
        return server;
    }

    private static void send(HttpExchange ex, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        ex.getResponseBody().write(bytes);
        ex.close();
    }

    public static void main(String[] args) throws IOException {
        String env = System.getenv("PORT");
        int port = (env == null || env.isEmpty()) ? 8080 : Integer.parseInt(env);
        start(port);
        System.out.println("Listening on port " + port);
    }
}
