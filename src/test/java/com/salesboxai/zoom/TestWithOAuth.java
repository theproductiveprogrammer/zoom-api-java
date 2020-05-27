package com.salesboxai.zoom;

import java.io.IOException;

/**
 * Test the Zoom API Library using an OAuth App
 *<p>
 * 		problem/<p>
 * To test OAuth, we need to have a webhook endpoint which can accept the zoom callback
 * and save the Oauth token. Then we can use this token to make zoom calls.
 *<p>
 * 		way/<p>
 * We can start this test with the saveZoomOAuthAccessToken() which starts a web server
 * that can be used as the webhook and then we can test the API calls (like getUserTest())
 * that will use the saved token. Set the clientID, clientSecret, and redirectURL in
 * `application.properties`.
 *
 * @author charles.lobo
 */
public class TestWithOAuth {

	/*		understand/
	 * Uncomment the functions one at a time to test.
	 */
	public static void main(String[] args) throws Exception {
		// saveZoomOAuthAccessToken();
		// getUserTest();
	}

	/*		outcome/
	 * Get the latest zoom OAuth configured saved by saveZoomOAuthAccessToken() below
	 * and use it to make a zoom call and retrieve details about the current user ("me")
	 */
	static void getUserTest() throws ZoomAPIException, IOException {
		SimplePersist db = new SimplePersist("oauth-token.db");
		ZoomAccessToken tkn = ZoomAccessToken.fromJSONString(db.loadLastEntry());
		if(tkn == null) throw new ZoomAPIException("No oauth token saved yet. Call saveZoomOAuthAccessToken() first");

		ZoomAuthorizerOAuth authorizer = new ZoomAuthorizerOAuth(tkn) {
			@Override
			public void onNewToken(ZoomAccessToken tkn) throws ZoomAPIException {
				try {
					db.save(tkn);
				} catch (Exception e) {
					System.err.println("Failed to save latest refreshed access token");
					e.printStackTrace();
				}
			}
		};
		ZoomAPI za = new ZoomAPI(authorizer);
		ZoomUser user = za.getUser("me");
		System.out.println(user);
	}

	/*		outcome/
	 * Start a simple web server that acts a as callback (use ngrok if needed)
	 * and saves the Oauth token in oauth-token.db.
	 */
	static void saveZoomOAuthAccessToken() throws Exception {
		Config cfg = new Config();

		String clientID = cfg.getValue("com.salesboxai.zoom.test.clientID");
		String clientSecret = cfg.getValue("com.salesboxai.zoom.test.clientSecret");
		String redirectURL = cfg.getValue("com.salesboxai.zoom.test.redirectURL");
		if(clientID == null || clientID.length() == 0
				|| clientSecret == null || clientSecret.length() == 0
				|| redirectURL == null || redirectURL.length() == 0) {
			throw new Exception("Please set the clientID, clientSecret, and redirectURL in application.properties");
		}

		ZoomAuthorizerGetOAuthToken authorizer = new ZoomAuthorizerGetOAuthToken(clientID, clientSecret);
		ZoomAPI za = new ZoomAPI(authorizer);

		SimplePersist db = new SimplePersist("oauth-token.db");

		int port = 80;
		try {
			port = Integer.parseInt(cfg.getValue("com.salesboxai.zoom.test.port"));
		} catch(Throwable t) {}

		new SimpleWebServer() {
			@Override
			protected void handleRequest(Request req, Response res) throws Exception {
				String code = req.params.get("code");
				if(code == null || code.length() == 0) res.status = 400;
				else {
					ZoomAccessToken tkn = za.requestAccessToken(code, redirectURL);
					db.save(tkn);
					System.out.println("Saved access token: " + tkn.toString());
				}
			}
		}.start(port);
		System.out.println("OAuth server started on port: " + port);
	}

}
