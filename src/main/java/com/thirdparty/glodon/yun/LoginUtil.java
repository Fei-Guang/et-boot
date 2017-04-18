package com.thirdparty.glodon.yun;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thirdparty.glodon.yun.utils.UrlBuilder;
import com.utils.Coder;
import com.utils.StringUtil;

public class LoginUtil {

	static Logger logger = LoggerFactory.getLogger(LoginUtil.class);

	/**
	 * 验证登录用户
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static String grant(String username, String password) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);
		return APIUtil.postResponseFromServer(
				UrlBuilder.buildGrantByPasswordUrl(), params);
	}

	/**
	 * 修改密码
	 * 
	 * @param oldPsw
	 * @param newPsw
	 * @param access_token
	 * @return
	 */
	public static String modifyPsw(String oldPsw, String newPsw,
			String access_token) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("p", oldPsw);
		params.put("np", Coder.URLEncoderUTF8(newPsw));
		return APIUtil.postResponseFromServer(
				UrlBuilder.buildModifyPasswordUrl(access_token), params,
				access_token);
	}

	/**
	 * 获取登录token
	 * 
	 * @param access_token
	 * @return
	 */
	public static String getLoginToken(String access_token) {
		return APIUtil.getResponseFromServer(UrlBuilder.buildLoginTokenUrl(),
				access_token);
	}

	/**
	 * 获取授权token信息
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public static String getAccessTokenInfo(String email, String password) {
		String access_token_json = grant(email, password);
		int count = 1;
		while (StringUtil.isNull(access_token_json)) {
			// 有时候广联云服务器来不及响应，获取到的结果为空，这里反复重试5次，直到获取的token不为空
			if (count > 5) {
				break;
			}
			access_token_json = grant(email, password);
			count++;
		}
		if (!StringUtil.isNull(access_token_json)) {
			return access_token_json;
		} else {
			logger.error("Failed to get access_token_json after 5 retry");
			return null;
		}
	}
}
