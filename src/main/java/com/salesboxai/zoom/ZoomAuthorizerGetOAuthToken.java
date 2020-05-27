package com.salesboxai.zoom;

/**
 * Use this authorizer when we need to access the ZoomAPI::requestAccessToken()
 *
 * @author charles.lobo
 */
public class ZoomAuthorizerGetOAuthToken extends ZoomAuthorizerOAuth {

	public ZoomAuthorizerGetOAuthToken(String clientId, String clientSecret) {
		super(clientId, clientSecret, null);
	}

	@Override
	public void onNewToken(ZoomAccessToken tkn) throws ZoomAPIException {
		throw new ZoomAPIException("No token to refresh. Perhaps you need ZoomAuthorizerOAuth");
	}

}
