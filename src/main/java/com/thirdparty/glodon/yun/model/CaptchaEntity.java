package com.thirdparty.glodon.yun.model;

import java.io.InputStream;

/**
 * 从广联云返回的图片验证码实体
 * 
 * @author zhouxing
 */
public class CaptchaEntity {

	private InputStream InputStream;
	private String captchaKey;

	public InputStream getInputStream() {
		return InputStream;
	}

	public void setInputStream(InputStream inputStream) {
		InputStream = inputStream;
	}

	public String getCaptchaKey() {
		return captchaKey;
	}

	public void setCaptchaKey(String captchaKey) {
		this.captchaKey = captchaKey;
	}
}
