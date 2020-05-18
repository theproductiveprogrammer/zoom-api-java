package com.salesboxai.zoom;

/**
 * Test the Zoom API
 */
public class ApiTest
{
	/*		outcome/
	 * Runs tests of the ZoomAPI and displays the output
	 */
	public static void main(String[] args) throws ZoomAPIException {
		getUserTest();
		scheduleMeetingTest();
	}

	/*		outcome/
	 * Get the zoom JWT token configured in `application.properties` and use it
	 * to make a zoom call and retrieve details about the current user ("me").
	 */
	private static void getUserTest() throws ZoomAPIException {
		ZoomAPI za = new ZoomAPI(new Config().getValue("com.salesboxai.zoom.test.jwtToken"));
		ZoomUser user = za.getUser("me");
		System.out.println(user);
	}

	private static void scheduleMeetingTest() throws ZoomAPIException {
		ZoomAPI za = new ZoomAPI(new Config().getValue("com.salesboxai.zoom.test.jwtToken"));
		ZoomMeetingRequest mreq = ZoomMeetingRequest.requestDefaults("Test Meeting", "Let's talk about the weather");
		ZoomMeeting meeting = za.createMeeting("me", mreq);
		System.out.println(meeting);
	}
}
