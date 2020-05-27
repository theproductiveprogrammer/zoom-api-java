package com.salesboxai.zoom;

/**
 * Represents a Zoom Meeting with all the meeting details.
 *
 * @author charles.lobo
 */
public class ZoomMeeting extends AsJsonString {
	public Long id;
	public String topic;
	public Integer type;
	public String start_time;
	public Integer duration;
	public String timezone;
	public String created_at;
	public String agenda;
	public String start_url;
	public String join_url;
	public String password;
	public String h323_password;
	public Long pmi;
	public TrackingField[] tracking_fields;
	public static class TrackingField {
		public String field;
		public String value;
	}
	public Occurence[] occurences;
	public static class Occurence {
		public String occurence_id;
		public String start_time;
		public Integer duration;
		public String status;
	}
	public Recurrence recurrence;
	public static class Recurrence {
		public Integer type;
		public Integer repeat_interval;
		public String weekly_days;
		public String monthly_day;
		public String monthly_week;
		public String monthly_week_day;
		public Integer end_times;
		public String end_date_time;
	}
	public Settings settings;
	public static class Settings {
		public Boolean host_video;
		public Boolean participant_video;
		public Boolean cn_meeting;
		public Boolean in_meeting;
		public Boolean join_before_host;
		public Boolean mute_upon_entry;
		public Boolean watermark;
		public Boolean use_pmi;
		public Integer approval_type;
		public Integer registration_type;
		public String audio;
		public String audio_recording;
		public Boolean enforce_login;
		public String enforce_login_domains;
		public String alternative_hosts;
		public Boolean close_registration;
		public Boolean waiting_room;
		public String[] global_dial_in_countries;
		public DialIn[] global_dial_in_numbers;
		public static class DialIn {
			public String country;
			public String country_name;
			public String city;
			public String number;
			public String type;
		}
		public String contact_name;
		public String contact_email;
		public Boolean registrants_confirmation_email;
		public Boolean registrants_email_notification;
		public Boolean meeting_authentication;
		public String authentication_option;
		public String authentication_domains;
		public String authentication_name;
		public String[] additional_data_center_regions;
	}

}
