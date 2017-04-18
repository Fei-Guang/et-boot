package com.thirdparty.glodon.yun.utils;

import com.thirdparty.glodon.yun.common.Constants;

/**
 * Basic的头信息
 * 
 * @author zhouxing 2014-3-26
 */
public class BasicAuthBuilder {

	/**
	 * 创建Basic认证头信息中的Key
	 * 
	 * @return
	 */
	public static String headerKey() {
		return "Authorization";
	}

	/**
	 * 创建Basic认证头信息中的Value
	 * 
	 * @return
	 */
	public static String headerValue() {
		String credential = Constants.SERVICE_KEY + ":"
				+ Constants.SERVER_SECRET;
		String encoder = Base64.encodeToString(credential);
		return "Basic " + encoder;
	}

	/**
	 * 创建Basic认证头信息中的Value
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String headerValue(String key, String value) {
		return key + " " + value;
	}
}
