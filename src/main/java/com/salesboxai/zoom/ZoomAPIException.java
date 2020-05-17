package com.salesboxai.zoom;

public class ZoomAPIException extends Exception {
	private static final long serialVersionUID = 7439457227993455235L;

	public ZoomAPIException(Throwable cause) {
		super(cause);
	}

	public ZoomAPIException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ZoomAPIException(String cause) {
		super(cause);
	}
}
