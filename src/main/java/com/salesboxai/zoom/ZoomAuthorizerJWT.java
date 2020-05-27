package com.salesboxai.zoom;

/**
 * The IZoomAuthorizer for JWT tokens
 *
 * @author charles.lobo
 */
public class ZoomAuthorizerJWT implements IZoomAuthorizer {

	String jwtToken;

	public ZoomAuthorizerJWT(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	@Override
	public String authHeader() {
		return "Bearer " + jwtToken;
	}

	@Override
	public boolean canRefresh() {
		return false;
	}

	@Override
	public ZoomAccessToken clearOAuthToken() {
		return null;
	}

	@Override
	public void onNewToken(ZoomAccessToken tkn) throws ZoomAPIException {
		throw new ZoomAPIException("JWT Tokens cannot be refreshed. Perhaps you need ZoomAuthorizerOAuth");
	}

}
