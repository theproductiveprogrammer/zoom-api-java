package com.salesboxai.zoom;

import java.util.Base64;

/**
 * This IZoomAuthorizer uses the ZoomAccessToken to authorize and
 * supports automatically refreshing the token when needed. When
 * the token is refreshed the `onNewToken(ZoomAccessToken tkn)` callback
 * is invoked so that users have a chance to save the new token
 * for later use.
 *
 * @author charles.lobo
 */
public abstract class ZoomAuthorizerOAuth implements IZoomAuthorizer {

	String clientId;
	String clientSecret;
	String appAuthToken;
	protected ZoomAccessToken oauth;

	public ZoomAuthorizerOAuth(String clientId, String clientSecret, ZoomAccessToken oauth) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		String key = clientId + ":" + clientSecret;
		this.appAuthToken = Base64.getEncoder().encodeToString(key.getBytes());

		this.oauth = oauth;
	}

	@Override
	public String authHeader() {
		if(oauth != null) return "Bearer " + oauth.access_token;
		else return "Basic " + appAuthToken;
	}

	@Override
	public boolean canRefresh() {
		return oauth != null;
	}

	@Override
	public ZoomAccessToken clearOAuthToken() {
		ZoomAccessToken old = oauth;
		this.oauth = null;
		return old;
	}

	protected void setNewOAuthToken(ZoomAccessToken oauth) {
		this.oauth = oauth;
	}
}
