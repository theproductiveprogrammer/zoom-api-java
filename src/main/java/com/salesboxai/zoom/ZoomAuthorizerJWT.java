package com.salesboxai.zoom;

/*		understand/
 * The ZoomAuthorizer for JWT tokens
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
	public ZoomAccessToken currentOAuthToken() {
		return null;
	}

	@Override
	public void onNewToken(ZoomAccessToken tkn) throws ZoomAPIException {
		throw new ZoomAPIException("JWT Tokens cannot be refreshed. Perhaps you need ZoomAuthorizerOAuth");
	}

}
