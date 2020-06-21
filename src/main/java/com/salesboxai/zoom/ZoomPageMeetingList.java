package com.salesboxai.zoom;

/**
 * Represents a Zoom Meeting List.
 *
 * @author charles.lobo
 */
public class ZoomPageMeetingList extends AsJsonString {
	public Integer page_count;
	public Integer page_number;
	public Integer page_size;
	public Integer total_records;

	public ZoomMeetingInfo[] meetings;
}
