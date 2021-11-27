package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;

public class FileHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
				String urlPath = exchange.getRequestURI().toString();
				if (urlPath == null || urlPath.equals("/")) {
					urlPath = "/index.html";
				}
				String filePath = "./web" + urlPath;
				File file = new File(filePath);
				OutputStream body = exchange.getResponseBody();
				if (file.exists()) {
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				}
				else {
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0); // should be HTTP_NOT_FOUND
					filePath = "./web/HTML/404.html";
					file = new File(filePath);
				}
				Files.copy(file.toPath(), body);
				body.close();
			}
			else {
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0); // should be HTTP_BAD_REQUEST
			}
		}
		catch (IOException e) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0); // should be HTTP_INTERNAL_ERROR
			exchange.getResponseBody().close();
			e.printStackTrace();
		}
	}
}
