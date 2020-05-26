package com.salesboxai.zoom;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/*		outcome/
 * This is a simple web server that can be used to handle web requests
 * Users are expected to override `handleRequest` to insert their own
 * business logic.
 */
public abstract class SimpleWebServer {
	static final int MAX_DATA_SIZE = 5 * 1024 * 1024;

	/*		outcome/
	 * Starts a server socket listening in the given port and accepts
	 * connections. Read in the request, call `handleRequest` to get
	 * a response and send it back.
	 * 
	 * This server doesn't scale by using threads/processes/nio
	 * because I want to keep it simple. Just handle the request then
	 * pick the next one.
	 */
	public void start(int port) throws IOException {
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("OAuthTest started on port: " + port);
		while(true) {
			Socket conn = null;
			try {
				conn = serverSocket.accept();
				Request req = new Request(conn);
				Response res = new Response();
				res.ver = req.ver;
				handleRequest(req, res);
				sendResponse(res, conn);
			} catch(Throwable t) {
				t.printStackTrace();
				sendErrorResponse(500, conn);
			}
			if(conn != null) {
				try {
					conn.close();
				} catch(Throwable t) {}
			}
		}
	}

	/*		understand/
	 * Override this method to hook in your own logic for request/response 
	 */
	protected abstract void handleRequest(Request req, Response res) throws Exception;

	/*		outcome/
	 * Send error response to client
	 */
	private void sendErrorResponse(int status, Socket conn) throws IOException {
		Response res = new Response();
		res.status = status;
		sendResponse(res, conn);
	}

	/*		outcome/
	 * Send the response back over the connection
	 */
	private void sendResponse(Response res, Socket conn) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());
		out.write(res.toString().getBytes());
		out.flush();
	}

	/*		understand/
	 * Represents the HTTP request split into
	 * 		- method
	 * 		- url
	 * 		- url path
	 * 		- url query parameters
	 * 		- ver (HTTP version)
	 * 		- headers
	 * 		- body
	 * This class can construct itself by reading from the connection
	 * socket and convert itself (toString()) into the valid HTTP
	 * representation.
	 */
	static class Request {
		public String method = "";
		public String url = "";
		public String path = "";
		public Map<String, String> params = new HashMap<String, String>();
		public String ver = "";
		public Map<String, String> headers = new HashMap<String, String>();
		public String body = "";

		Request(Socket conn) throws IOException {
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
				if(total > MAX_DATA_SIZE) break;
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
			if(url != null && url.length() != 0) {
				String[] pq = url.split("\\?", 2);
				path = pq[0];
				if(pq.length == 2) {
					String[] qs = pq[1].split("&");
					for(int i = 0;i < qs.length;i++) {
						String[] kv = qs[i].split("=", 2);
						if(kv.length == 2) {
							params.put(URLDecoder.decode(kv[0], "UTF-8"), URLDecoder.decode(kv[1], "UTF-8"));
						}
					}
				}
			}
			for(int i = 1;i < lines.length;i++) {
				String[] kv = lines[i].split(":", 2);
				if(kv.length == 2) headers.put(kv[0].trim(), kv[1].trim());
				else System.err.println("Did not understand header: " + lines[i]);
			}
		}

		public String toString() {
			StringBuffer s = new StringBuffer(); 
			s.append(method);
			s.append(" ");
			s.append(url);
			s.append(" ");
			s.append(ver);
			for(String k : headers.keySet()) {
				s.append("\r\n");
				s.append(k);
				s.append(": ");
				s.append(headers.get(k));
			}
			s.append("\r\n");
			if(body != null) {
				s.append("\r\n");
				s.append(body);
			}
			return s.toString();
		}
	}

	/*		understand/
	 * Represents a HTTP Response to be sent back to the server
	 * containing:
	 * 		- status (HTTP status)
	 * 		- statusMsg (optional short additional status message)
	 * 		- ver (HTTP version)
	 * 		- headers
	 * 		- body
	 * This class can convert itself into a valid HTTP representation
	 * to be sent back to the client.
	 */
	static class Response {
		public int status;
		public String statusMsg;
		public String ver;
		public Map<String, String> headers = new HashMap<String, String>();
		public String body;

		public String toString() {
			if(ver == null) ver = "HTTP/1.0";
			if(status == 0) {
				status = 200;
			}
			if(statusMsg == null) {
				if(status > 299) statusMsg = "Error";
				else statusMsg = "Ok";
			}
			StringBuffer s = new StringBuffer();
			s.append(ver);
			s.append(" ");
			s.append(status);
			s.append(" ");
			s.append(statusMsg);

			if(body != null) headers.put("Content-Length", Integer.toString(body.length()));
			for(String k : headers.keySet()) {
				s.append("\r\n");
				s.append(k);
				s.append(": ");
				s.append(headers.get(k));
			}
			s.append("\r\n");
			if(body != null) {
				s.append("\r\n");
				s.append(body);
			}
			return s.toString();
		}
	}
}
