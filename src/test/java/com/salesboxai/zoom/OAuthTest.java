package com.salesboxai.zoom;

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

		new SimpleWebServer() {
			@Override
			protected void handleRequest(Request req, Response res) throws Exception {
				System.out.println(req.toString());
				res.status = 200;
				res.body = "All Good!";
			}
		}.start(port);
		System.out.println("OAuth server started on port: " + port);
	}

}
