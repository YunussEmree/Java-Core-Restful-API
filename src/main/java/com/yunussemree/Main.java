package com.yunussemree;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.yunussemree.handler.CorsHandler;
import com.yunussemree.handler.UserHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final int PORT = 8000;
    private static final String STATIC_DIR = "src/main/resources/static";

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/api/users", new CorsHandler(new UserHandler()));
        server.createContext("/", new StaticFileHandler());
        server.createContext("/index.html", new RedirectHandler("/"));

        server.setExecutor(null);
        server.start();

        logger.info("Server is running on port " + PORT);
        System.out.println("Server is running on port " + PORT);
        System.out.println("Open http://localhost:" + PORT + " in your browser");
    }

    static class StaticFileHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();

            if ("/".equals(path)) {
                path = "/index.html";
            }

            Path filePath = Paths.get(STATIC_DIR, path);
            File file = filePath.toFile();

            if (!file.exists() || file.isDirectory()) {
                String response = "404 (Not Found)";
                exchange.sendResponseHeaders(404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
                return;
            }

            String contentType = getContentType(path);
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, file.length());
            Files.copy(file.toPath(), exchange.getResponseBody());
            exchange.getResponseBody().close();
        }

        private String getContentType(String path) {
            if (path.endsWith(".html")) {
                return "text/html";
            } else if (path.endsWith(".css")) {
                return "text/css";
            } else if (path.endsWith(".js")) {
                return "application/javascript";
            } else if (path.endsWith(".json")) {
                return "application/json";
            } else if (path.endsWith(".png")) {
                return "image/png";
            } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                return "image/jpeg";
            } else if (path.endsWith(".svg")) {
                return "image/svg+xml";
            }
            return "application/octet-stream";
        }
    }

    static class RedirectHandler implements HttpHandler {

        private final String location;

        public RedirectHandler(String location) {
            this.location = location;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Location", location);
            exchange.sendResponseHeaders(302, -1);
            exchange.getResponseBody().close();
        }
    }
}
