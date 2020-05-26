package com.salesboxai.zoom;

/*		understand/
 * This IZoomAuthorizer uses the ZoomAccessToken to authorize and
 * supports automatically refreshing the token when needed. When
 * the token is refreshed the `onNewToken(ZoomAccessToken tkn)` callback
 * is invoked so that users have a chance to save the new token
 * for later use.
 */
public abstract class ZoomAuthorizerOAuth implements IZoomAuthorizer {

	protected ZoomAccessToken oauth;

	public ZoomAuthorizerOAuth(ZoomAccessToken oauth) {
		this.oauth = oauth;
	}

	@Override
	public String authHeader() {
		return "Bearer " + oauth.access_token;
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	public ZoomAccessToken currentOAuthToken() {
		return oauth;
	}

	protected void setCurrentOAuthToken(ZoomAccessToken oauth) {
		this.oauth = oauth;
	}
}
