package com.salesboxai.zoom;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/*		understand/
 * Handle the OAuth callback by writing a simple web server that can understand
 * the Zoom Oauth callback.
 */
public class OAuthTest {

	public static void main(String[] args) throws Exception {
		Config cfg = new Config();
		int port = 80;
		try {
			port = Integer.parseInt(cfg.getValue("com.salesboxai.zoom.test.port"));
		} catch(Throwable t) {}
		
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("OAuthTest started on port: " + port);
		while(true) {
			Socket conn = null;
			try {
				conn = serverSocket.accept();
				Request req = new Request(cfg, conn);
				System.out.println(req);
				sendResponse(req.ver, 200, "All Ok!", conn);
			} catch(Throwable t) {
				System.err.println(t);
			}
			if(conn != null) {
				try {
					conn.close();
				} catch(Throwable t) {}
			}
		}
	}

	static void sendResponse(String ver, int status, String body, Socket conn) throws IOException {
		PrintStream out = new PrintStream(new BufferedOutputStream(conn.getOutputStream()));
		out.print(ver);
		out.print(" ");
		out.print(status);
		if(status == 200) out.print(" Ok");
		else out.print(" Error");
		if(body == null) out.print("\r\nContent-Length: 0");
		else out.print("\r\nContent-Length: " + body.length());
		out.print("\r\n\r\n");
		out.print(body);
		out.flush();
	}

	static class Request {
		String method = "";
		String url = "";
		String ver = "";
		Map<String, String> headers = new HashMap<String, String>();
		String body = "";
		Request(Config cfg, Socket conn) throws IOException {
			InputStream in = conn.getInputStream();
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length, total = 0;
			while(true) {
				if(in.available() == 0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
					if(in.available() == 0) break;
				}
				length = in.read(buffer);
				if(length == -1) break;
				data.write(buffer, 0, length);
				total += length;
				if(total > cfg.MAX_RESPONSE_SIZE) break;
			}

			String[] req = data.toString("UTF-8").split("\r\n\r\n", 2);
			if(req.length == 2) body = req[1];
			String[] lines = req[0].split("\r\n");
			String[] startline = lines[0].split(" ");
			if(startline.length != 3) System.err.println("Incorrect start line: " + lines[0]);
			else {
				method = startline[0];
				url = startline[1];
				ver = startline[2];
			}
			for(int i = 1;i < lines.length;i++) {
				String[] kv = lines[i].split(":", 2);
				if(kv.length == 2) headers.put(kv[0].trim(), kv[1].trim());
				else System.err.println("Did not understand header: " + lines[i]);
			}
		}

		public String toString() {
			String s = method;
			for(String k : headers.keySet()) {
				s += "\r\n" + k + ":" + headers.get(k);
			}
			if(body != null) s += "\r\n" + body;
			return s;
		}
	}
}
