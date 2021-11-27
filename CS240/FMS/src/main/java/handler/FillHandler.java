package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.MessageResult;
import result.Result;
import service.FillService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
				FillService fs = new FillService();
				String[] args = exchange.getRequestURI().toString().split("(?!^)/");
				MessageResult result;
				if (args.length == 3) {
					result = fs.fill(args[1], Integer.parseInt(args[2]));
				}
				else if (args.length == 2) {
					result = fs.fill(args[1], 4);
				}
				else {
					result = new MessageResult("Invalid parameters. Please include at least the username and optionally number of generations to fill.");
				}
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
