package com.salesboxai.zoom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * This is a base class that can be used by POJO's to show
 * their output as a nice readable JSON.
 *
 * @author charles.lobo
 */
public class AsJsonString {

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
