package com.salesboxai.zoom;

/*		understand/
 * This class represents the ability to authorize Zoom requests. It has
 * three available sub classes that should be used in different cases:
 * 		1. ZoomAuthorizerJWT : to authorize using JWT for server-to-server API calls
 * 		2. ZoomAuthorizerOauth : to authorize using Oauth with access token and automatic refresh
 * 		3. ZoomAuthorizerGetOAuthToken : to authorize using clientID/clientSecret to create the user's access token 
 */
public interface IZoomAuthorizer {
	public String authHeader();
	public boolean canRefresh();
	public ZoomAccessToken currentOAuthToken();
	public void onNewToken(ZoomAccessToken tkn) throws ZoomAPIException;
}
