package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.Result;
import service.EventService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
				Headers reqHeaders = exchange.getRequestHeaders();
				if (reqHeaders.containsKey("Authorization")) {
					String authToken = reqHeaders.getFirst("Authorization");
					EventService es = new EventService();
					String[] args = exchange.getRequestURI().toString().split("(?!^)/");
					Result eventResult;
					if (args.length > 1) {
						eventResult = es.getEventByID(args[1], authToken);
					}
					else {
						eventResult = es.getEventsByPerson(authToken);
					}
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
					OutputStream body = exchange.getResponseBody();
					String response = Handler.serializeObject(eventResult);
					Handler.writeString(response, body);
					body.close();
				}
				else {
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
					exchange.getResponseBody().close();
				}
			}
			else {
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				exchange.getResponseBody().close();
			}
		}
		catch (IOException e) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
			exchange.getResponseBody().close();
			e.printStackTrace();
		}
	}
}
