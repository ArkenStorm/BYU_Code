package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.MessageResult;
import service.ClearService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class ClearHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
				ClearService cs = new ClearService();
				MessageResult result = cs.clear();
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				OutputStream body = exchange.getResponseBody();
				String response = Handler.serializeObject(result);
				Handler.writeString(response, body);
				body.close();
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
