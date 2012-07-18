package com.ewcms.web.filter;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CaptchaUsernamePasswordToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 5476815673987468588L;

	private String captcha;

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public CaptchaUsernamePasswordToken() {
		super();
	}

	public CaptchaUsernamePasswordToken(String username, char[] password, boolean rememberMe, String host, String captcha) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;

	}
}
