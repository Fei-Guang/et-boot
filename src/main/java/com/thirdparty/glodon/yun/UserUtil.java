package com.thirdparty.glodon.yun;

import com.thirdparty.glodon.yun.utils.UrlBuilder;

public class UserUtil {

	/**
	 * 获取用户信息
	 * 
	 * @param access_token
	 * @return
	 */
	public static String getUserInfo(String access_token) {
		return APIUtil.getResponseFromServer(UrlBuilder.buildGetUserUrl(),
				access_token);
	}

	/**
	 * 批量获取用户ID
	 * 
	 * @param identity
	 *            多个参数可以用逗号(,)分隔开来，但是一次最多只能50个
	 * @return
	 */
	public static String getUserID(String identity) {
		return APIUtil.getResponseFromServer(UrlBuilder
				.buildGetUserIDUrl(identity));
	}
}
