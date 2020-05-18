package com.salesboxai.zoom;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 		understand/
 * Provides a Java wrapper around the Zoom API, returning Java objects
 * for easy use in code.
 */
public class ZoomAPI
{
	private Config cfg;
	private String jwtToken;

	public ZoomAPI(String jwtToken) {
		cfg = new Config();
		this.jwtToken = jwtToken;
	}

	public ZoomUser getUser(String id) throws ZoomAPIException {
		String url = endpoint("/users/" + id);
		return get(url, ZoomUser.class);
	}

	public ZoomMeeting createMeeting(String user, ZoomMeetingRequest mreq) throws ZoomAPIException {
		String url = endpoint("/users/" + user + "/meetings");
		return post(url, mreq.toString(), ZoomMeeting.class);
	}

	private String endpoint(String name) {
		return cfg.API_ENDPOINT + "/" + name;
	}

	public <T> T post(String url, String reqJSON, Class<T> cls) throws ZoomAPIException {
		return call("POST", url, reqJSON, cls);
	}

	public <T> T get(String url, Class<T> cls) throws ZoomAPIException {
		return call("GET", url, null, cls);
	}

	/*		outcome/
	 * Make a generic call to the given URL with the given method and body, read
	 * the response as a JSON and convert it into the requested class type.
	 */
	public <T> T call(String method, String url, String reqJSON, Class<T> cls) throws ZoomAPIException {
		try {

			URL url_ = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) url_.openConnection();
			conn.setRequestMethod(method);
			conn.setRequestProperty("authorization", "Bearer " + jwtToken);

			if(reqJSON != null && reqJSON.length() > 0) {
				OutputStream out = null;
				try {
					conn.setRequestProperty("content-type", "application/json");
					conn.setDoOutput(true);
					out = conn.getOutputStream();
					out.write(reqJSON.getBytes("UTF-8"));
					out.flush();
					out.close();
				} catch(Throwable t) {
					if(out != null) out.close();
					throw new ZoomAPIException(t);
				}

			}

			int status = conn.getResponseCode();
			InputStream in = status > 299 ? conn.getErrorStream() : conn.getInputStream();
			ByteArrayOutputStream body = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length, total = 0;
			while((length = in.read(buffer)) != -1) {
				body.write(buffer, 0, length);
				total += length;
				if(total > cfg.MAX_RESPONSE_SIZE) {
					String cl = conn.getHeaderField("Content-Length");
					in.close();
					String xtract = body.toString("UTF-8");
					if(xtract.length() > 256) xtract = xtract.substring(0, 256);
					throw new ZoomAPIException("Response too big (Content-Length: " + cl + "). Read: " + total + " bytes\n" + xtract);
				}
			}
			in.close();

			if(status > 299) {
				throw new ZoomAPIException("Call failed with status: " + status + " " + body.toString("UTF-8"));
			}

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(body.toByteArray(), cls);

		} catch (Throwable e) {
			throw new ZoomAPIException(e);
		}
	}

}
