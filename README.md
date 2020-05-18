# Java Zoom API

This is a wrapper around the Zoom API that allows us to call it using native Java code.

![logo](./zoom-api-java.png)

## How to Use

Create an `application.properties` with your configuration parameters (TODO: improve documentation. For now check the code).

```java
String jwtToken = new Config().getValue("com.salesboxai.zoom.test.jwtToken")
ZoomAPI za = new ZoomAPI(jwtToken);

// Get User
ZoomUser user = za.getUser("me");
System.out.println(user);

// Create a new meeting
ZoomMeetingRequest mreq = ZoomMeetingRequest.requestDefaults("Test Meeting", "Let's talk about the weather");
ZoomMeeting meeting = za.createMeeting("me", mreq);
System.out.println(meeting);

```

