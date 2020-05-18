package com.salesboxai.zoom;

import com.salesboxai.zoom.ZoomMeeting.Recurrence;
import com.salesboxai.zoom.ZoomMeeting.Settings;
import com.salesboxai.zoom.ZoomMeeting.TrackingField;

/*		understand/
 * Represents a request for a Zoom meeting
 */
public class ZoomMeetingRequest extends AsJsonString {
	public String topic;
	public Integer type;
	public String start_time;
	public Integer duration;
	public String schedule_for;
	public String timezone;
	public String password;
	public String agenda;
	public TrackingField[] tracking_fields;
	public Recurrence recurrence;
	public Settings settings;

	public static ZoomMeetingRequest requestDefaults(String topic, String agenda) {
		ZoomMeetingRequest zmr = new ZoomMeetingRequest();
		zmr.topic = topic;
		zmr.type = 1;
		zmr.agenda = agenda;
		zmr.settings = new ZoomMeeting.Settings();
		zmr.settings.join_before_host = true;
		zmr.settings.approval_type = 2;
		zmr.settings.meeting_authentication = false;
		return zmr;
	}
}
