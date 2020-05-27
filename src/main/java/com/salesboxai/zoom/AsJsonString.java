package com.salesboxai.zoom;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * This is a base class that can be used by POJO's to show
 * their output as a nice readable JSON.
 *
 * @author charles.lobo
 */
public class AsJsonString {

	protected static <T> T fromString(String json, Class<T> cls) throws ZoomAPIException {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(json.getBytes("UTF-8"), cls);
		} catch(Throwable e) {
			throw new ZoomAPIException(e);
		}
	}

	/**
	 * Convert this class into a nice JSON representation string
	 */
	public String toString() {
		String str = this.getClass().getTypeName();
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			str = mapper.writeValueAsString(this);
		} catch (Throwable e) { /* ignore */ }
		return str;
	}
}
