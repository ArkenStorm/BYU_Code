package handler;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Handler {
	public static void writeString(String str, OutputStream os) throws IOException {
		OutputStreamWriter sw = new OutputStreamWriter(os);
		BufferedWriter bw = new BufferedWriter(sw);
		bw.write(str);
		bw.flush();
		bw.close();
		sw.close();
	}

	public static String serializeObject(Object o) {
		Gson gson = new Gson();
		String json = gson.toJson(o);
		return json;
	}
}
