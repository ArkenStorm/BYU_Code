package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.Result;
import service.PersonService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class PersonHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
				Headers reqHeaders = exchange.getRequestHeaders();
				if (reqHeaders.containsKey("Authorization")) {
					String authToken = reqHeaders.getFirst("Authorization");
					PersonService ps = new PersonService();
					String[] args = exchange.getRequestURI().toString().split("(?!^)/");
					Result personResult;
					if (args.length > 1) {
						personResult = ps.getPersonByID(authToken, args[1]);
					}
					else {
						personResult = ps.getPersons(authToken);
					}
					exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
					OutputStream body = exchange.getResponseBody();
					String response = Handler.serializeObject(personResult);
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
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			exchange.getResponseBody().close();
			e.printStackTrace();
		}
	}
}
