package com.salesboxai.zoom;

/**
 * Represents the result of a request for list of Zoom meetings
 * 
 * @author charles.lobo
 */
public class ZoomMeetingInfo extends AsJsonString {
	public String uuid;
	public String id;
	public String host_id;
	public String topic;
	public Integer type;
	public String start_time;
	public Integer duration;
	public String timezone;
	public String created_at;
	public String join_url;
	public String agenda;
}