package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoadRequest;
import result.Result;
import service.LoadService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoadHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		try {
			if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
				LoadService ls = new LoadService();
				LoadRequest request = new Gson().fromJson(new InputStreamReader(exchange.getRequestBody()), LoadRequest.class);
				Result loadResult = ls.loadDatabase(request);
				exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				OutputStream body = exchange.getResponseBody();
				String response = Handler.serializeObject(loadResult);
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
