package com.example.fmc;

import android.util.Log;
import result.*;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerProxy {
	private static final String LOG_TAG = "Proxy";

	public static Result authorizeUser(Object request, URL url) {
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			OutputStream os = connection.getOutputStream();
			os.write(new Gson().toJson(request).getBytes());
			os.close();
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String outString = ServerProxy.createResponse(connection);
				if (outString.contains("message")) {
					return new Gson().fromJson(outString, MessageResult.class);
				}
				return new Gson().fromJson(outString, AuthorizationResult.class);
			}
		} catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
		}

		return null;
	}

	public static Result getPersons(URL url) {
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", Datacache.getInstance().getToken());
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String outString = ServerProxy.createResponse(connection);
				if (outString.contains("message")) {
					return new Gson().fromJson(outString, MessageResult.class);
				}
				return new Gson().fromJson(outString, PersonArrayResult.class);
			}
		} catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
		}

		return null;
	}

	public static Result getEvents(URL url) {
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", Datacache.getInstance().getToken());
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String outString = ServerProxy.createResponse(connection);
				if (outString.contains("message")) {
					return new Gson().fromJson(outString, MessageResult.class);
				}
				return new Gson().fromJson(outString, EventArrayResult.class);
			}
		} catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage(), e);
		}

		return null;
	}

	private static String createResponse(HttpURLConnection connection) throws IOException {
		InputStream responseBody = connection.getInputStream();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = responseBody.read(buffer)) != -1) {
			outputStream.write(buffer, 0, length);
		}
		return outputStream.toString();
	}
}
