package com.salesboxai.zoom;

/**
 * Represents a Zoom Access Token.
 *
 * @author charles.lobo
 */
public class ZoomAccessToken extends AsJsonString {
	public String access_token;
	public String token_type;
	public String refresh_token;
	public String scope;
}
