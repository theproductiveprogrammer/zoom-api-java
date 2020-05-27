package com.salesboxai.zoom;

import java.util.Base64;

/**
 * Use this authorizer when we need to access the ZoomAPI::requestAccessToken()
 *
 * @author charles.lobo
 */
public class ZoomAuthorizerGetOAuthToken implements IZoomAuthorizer {
	
	String clientId;
	String clientSecret;
	String authToken;

	public ZoomAuthorizerGetOAuthToken(String clientId, String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		String key = clientId + ":" + clientSecret;
		this.authToken = Base64.getEncoder().encodeToString(key.getBytes());
	}

	@Override
	public String authHeader() {
		return "Basic " + authToken;
	}

	@Override
	public boolean canRefresh() {
		return false;
	}

	@Override
	public ZoomAccessToken currentOAuthToken() {
		return null;
	}

	@Override
	public void onNewToken(ZoomAccessToken tkn) throws ZoomAPIException {
		throw new ZoomAPIException("No token to refresh. Perhaps you need ZoomAuthorizerOAuth");
	}

}
