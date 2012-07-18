package com.ewcms.security.service;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 5024174467050386806L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
