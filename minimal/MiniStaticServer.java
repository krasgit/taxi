import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MiniStaticServer {



    public static void main(String[] args) throws Exception {
        int port = 9999;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new FileHandler());
        server.setExecutor(null); // default executor
        System.out.println("Serving files from current directory on http://localhost:" + port);
        server.start();
    }


    private static final String OSRM_BACKEND = "http://127.0.0.1:5000";
    
        private static void proxyToOsrm(HttpExchange exchange, String osrmPath) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String forwardUrl = OSRM_BACKEND + osrmPath + (query != null ? "?" + query : "");

        System.out.println("Proxy → " + forwardUrl);

        // Open connection to OSRM backend
        java.net.URL url = new java.net.URL(forwardUrl);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        int status = conn.getResponseCode();

        InputStream is = status >= 200 && status < 300
                ? conn.getInputStream()
                : conn.getErrorStream();

        byte[] response = is.readAllBytes();
        is.close();

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, response.length);

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    static class FileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();


        // ---- PROXY MODE ----
        if (path.startsWith("/osrm/")) {
            proxyToOsrm(exchange, path.replace("/osrm", ""));
            return;
        }


            if (path.equals("/")) {
                path = "/index.html"; // default file
            }

            File file = new File("." + path); // current directory
            if (file.exists() && file.isFile()) {
                byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));
                exchange.getResponseHeaders().add("Content-Type", guessContentType(file.getName()));
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            } else {
                String notFound = "<h1>404 - File Not Found</h1>";
                exchange.sendResponseHeaders(404, notFound.length());
                OutputStream os = exchange.getResponseBody();
                os.write(notFound.getBytes());
                os.close();
            }
        }

        private String guessContentType(String filename) {
            if (filename.endsWith(".html")) return "text/html";
            if (filename.endsWith(".css")) return "text/css";
            if (filename.endsWith(".js")) return "application/javascript";
            if (filename.endsWith(".png")) return "image/png";
            if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
            return "application/octet-stream";
        }
    }
}

