package com.salesboxai.zoom;

import java.io.InputStream;
import java.util.Properties;

/*		understand/
 * Load configuration parameters from `application.properties`
 * defaulting if not provided.
 * 
 * 		com.salesboxai.zoom.MAX_RESPONSE_SIZE  (default 5MB)
 * 		com.salesboxai.zoom.API_ENDPOINT (default https://api.zoom.us/v2)
 * 		com.salesboxai.zoom.OAUTH_ENDPOINT (default https://zoom.us/oauth/token)
 */
class Config {
	public int MAX_RESPONSE_SIZE;
	public String API_ENDPOINT;
	public String OAUTH_ENDPOINT;
	private Properties props;

	public Config() {

		MAX_RESPONSE_SIZE = 5 * 1024 * 1024;
		API_ENDPOINT = "https://api.zoom.us/v2";
		OAUTH_ENDPOINT = "https://zoom.us/oauth/token";

		try {
			InputStream in = Config.class.getClassLoader().getResourceAsStream("application.properties");
			props = new Properties();
			props.load(in);
			
			String s = props.getProperty("com.salesboxai.zoom.MAX_RESPONSE_SIZE");
			if(s != null) {
				try {
					MAX_RESPONSE_SIZE = Integer.parseInt(s);
				} catch (NumberFormatException e) { /* ignore */ }
			}

			s = props.getProperty("com.salesboxai.zoom.API_ENDPOINT");
			if(s != null) API_ENDPOINT = s;

			s = props.getProperty("com.salesboxai.zoom.OAUTH_ENDPOINT");
			if(s != null) OAUTH_ENDPOINT = s;

		} catch(Throwable t) {
			/* ignore errors - just default */
		}
	}

	/*		outcome/
	 * Return a generic value stored in application properties
	 * Useful mostly for testing.
	 */
	public String getValue(String name) {
		if(props == null) return null;
		return props.getProperty(name);
	}
}
