package com.salesboxai.zoom;

/**
 * This class represents the ability to authorize Zoom requests. It has
 * three available sub classes that should be used in different cases:
 * <ol>
 * 		<li> ZoomAuthorizerJWT : to authorize using JWT for server-to-server API calls
 * 		<li> ZoomAuthorizerOauth : to authorize using Oauth with access token and automatic refresh
 * 		<li> ZoomAuthorizerGetOAuthToken : to authorize using clientID/clientSecret to create the user's access token
 * </ol>
 * 
 * @author charles.lobo
 */
public interface IZoomAuthorizer {
	public String authHeader();
	public boolean canRefresh();
	public ZoomAccessToken currentOAuthToken();
	public void onNewToken(ZoomAccessToken tkn) throws ZoomAPIException;
}
