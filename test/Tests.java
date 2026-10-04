import com.sun.net.httpserver.HttpServer;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Tests {
    static int passed = 0, total = 0;

    static void check(String name, boolean ok) {
        total++;
        if (ok) passed++;
        else System.err.println("FAILED: " + name);
    }

    public static void main(String[] args) throws Exception {
        HttpServer server = App.start(0); // random free port
        int port = server.getAddress().getPort();
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> r = get(client, port, "/");
        check("root returns welcome", r.statusCode() == 200 && r.body().contains("Welcome"));

        r = get(client, port, "/healthz");
        check("healthz returns 200 OK", r.statusCode() == 200 && r.body().equals("OK"));

        r = get(client, port, "/quote");
        check("quote is from the list", r.statusCode() == 200 && App.QUOTES.contains(r.body()));

        boolean allValid = true;
        for (int i = 0; i < 20; i++) {
            allValid &= App.QUOTES.contains(get(client, port, "/quote").body());
        }
        check("20 quotes are all valid", allValid);

        r = get(client, port, "/nope");
        check("unknown path is 404", r.statusCode() == 404);

        server.stop(0);
        System.out.println("TESTS: " + passed + "/" + total);
        System.exit(passed == total ? 0 : 1);
    }

    static HttpResponse<String> get(HttpClient c, int port, String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).build();
        return c.send(req, HttpResponse.BodyHandlers.ofString());
    }
}
