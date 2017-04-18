package com.thirdparty.glodon.yun;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thirdparty.glodon.yun.model.CaptchaEntity;
import com.thirdparty.glodon.yun.model.InviteUserDomain;
import com.thirdparty.glodon.yun.model.InvitedUser;
import com.thirdparty.glodon.yun.model.UserDomain;
import com.thirdparty.glodon.yun.utils.BasicAuthBuilder;
import com.thirdparty.glodon.yun.utils.UrlBuilder;
import com.utils.StringUtil;
import com.utils.url.HttpClient_Get_Post;

public class RegisterUtil {

	private static Logger logger = LoggerFactory.getLogger(RegisterUtil.class);

	public static final String RESULT_200 = "valid";
	public static final String RESULT_201 = "success";
	public static final String RESULT_400 = "invalid_name";
	public static final String RESULT_401 = "auth_fail";
	public static final String RESULT_406 = "failed";
	public static final String RESULT_409 = "name_exist";
	public static final String RESULT_423 = "name_not_verified";
	public static final String RESULT_other = "other";

	public static final String CAPTCHA_HEADER_KEY = "Signup-Key";

	/**
	 * 验证用户名是否合法
	 * 
	 * @param name
	 * @return
	 */
	public static String check(String name) {
		try {
			int code = APIUtil.getResponseCodeFromServer(UrlBuilder
					.buildNameCheckerUrl(name));
			// 处理返回
			if (HttpStatus.SC_OK == code) {
				return RESULT_200;
			} else if (HttpStatus.SC_UNAUTHORIZED == code) {
				return RESULT_401;
			} else if (HttpStatus.SC_BAD_REQUEST == code) {
				return RESULT_400;
			} else if (HttpStatus.SC_CONFLICT == code) {
				return RESULT_409;
			} else if (HttpStatus.SC_LOCKED == code) {
				return RESULT_423;
			} else {
				return RESULT_other;
			}
		} catch (Exception e) {
			logger.error("Check:", e);
		}
		return RESULT_other;
	}

	/**
	 * 注册用户，创建用户账户
	 * 
	 * @param userDomain
	 * @return
	 */
	public static String create(UserDomain userDomain) {
		JSONObject jo = new JSONObject();
		// 校验用户的合法性
		if (!validate(userDomain)) {
			jo.put("ret", RESULT_other);
		} else {
			HttpClient_Get_Post httpClient_Get_Post = new HttpClient_Get_Post();
			try {
				Map<String, String> requestProperty = new HashMap<String, String>();
				requestProperty.put(BasicAuthBuilder.headerKey(),
						BasicAuthBuilder.headerValue());
				requestProperty.put(CAPTCHA_HEADER_KEY,
						userDomain.getCaptchaKey());
				HttpResponse response = httpClient_Get_Post.doPost(
						UrlBuilder.buildSignupUrl(), convert2Json(userDomain),
						ContentType.APPLICATION_JSON, null, requestProperty);
				// 处理返回
				int code = response.getStatusLine().getStatusCode();
				// 处理返回
				if (HttpStatus.SC_CREATED == code) {
					String res = EntityUtils.toString(response.getEntity());
					jo = JSON.parseObject(res);
					jo.put("ret", RESULT_201);
				} else if (HttpStatus.SC_UNAUTHORIZED == code) {
					jo.put("ret", RESULT_401);
				} else if (HttpStatus.SC_NOT_ACCEPTABLE == code) {
					jo.put("ret", RESULT_406);
				} else {
					jo.put("ret", RESULT_other);
				}
			} catch (ClientProtocolException e) {
				logger.error("Create:", e);
				jo.put("ret", RESULT_other);
			} catch (IOException e) {
				logger.error("Create:", e);
				jo.put("ret", RESULT_other);
			} catch (Exception e) {
				logger.error("Create:", e);
				jo.put("ret", RESULT_other);
			} finally {
				httpClient_Get_Post.shutdown();
			}
		}
		return jo.toJSONString();
	}

	/**
	 * 邀请用户，主动为用户创建广联云账户
	 * 
	 * @param inviteUserDomain
	 * @param access_token
	 * @return
	 */
	public static String invite(InviteUserDomain inviteUserDomain,
			String access_token) {
		JSONObject jo = new JSONObject();
		// 校验用户的合法性
		if (!validate(inviteUserDomain)) {
			jo.put("ret", RESULT_other);
		} else {
			HttpClient_Get_Post httpClient_Get_Post = new HttpClient_Get_Post();
			try {
				Map<String, String> requestProperty = new HashMap<String, String>();
				requestProperty.put(BasicAuthBuilder.headerKey(),
						BasicAuthBuilder.headerValue("Bearer", access_token));
				HttpResponse response = httpClient_Get_Post.doPost(
						UrlBuilder.buildInviteUrl(),
						JSON.toJSONString(inviteUserDomain),
						ContentType.APPLICATION_JSON, null, requestProperty);
				// 处理返回
				int code = response.getStatusLine().getStatusCode();
				if (HttpStatus.SC_OK == code) {
					String res = EntityUtils.toString(response.getEntity());
					JSONArray ja = JSON.parseArray(res);
					jo.put("inviteUser", ja);
					jo.put("ret", RESULT_200);
				} else if (HttpStatus.SC_UNAUTHORIZED == code) {
					jo.put("ret", RESULT_401);
				} else if (HttpStatus.SC_BAD_REQUEST == code) {
					jo.put("ret", RESULT_400);
				} else {
					jo.put("ret", RESULT_other);
				}
			} catch (ClientProtocolException e) {
				logger.error("Invite:", e);
				jo.put("ret", RESULT_other);
			} catch (IOException e) {
				logger.error("Invite:", e);
				jo.put("ret", RESULT_other);
			} catch (Exception e) {
				logger.error("Invite:", e);
				jo.put("ret", RESULT_other);
			} finally {
				httpClient_Get_Post.shutdown();
			}
		}
		return jo.toJSONString();
	}

	/**
	 * 获取验证码图片流
	 * 
	 * @return
	 */
	public static CaptchaEntity fetch() {
		HttpClient_Get_Post httpClient_Get_Post = new HttpClient_Get_Post();
		try {
			Map<String, String> requestProperty = new HashMap<String, String>();
			requestProperty.put(BasicAuthBuilder.headerKey(),
					BasicAuthBuilder.headerValue());
			HttpResponse response = httpClient_Get_Post.doGet(
					UrlBuilder.buildCaptchaFetcherUrl(), null, null,
					requestProperty);
			if (HttpStatus.SC_UNAUTHORIZED == response.getStatusLine()
					.getStatusCode()) {
				logger.error("response={}", response);	
				return null;
			}
			// 获取返回图片验证码对应的key值
			String captchaKey = response.getFirstHeader(CAPTCHA_HEADER_KEY)
					.getValue();
			// 构造返回值
			CaptchaEntity captchaEntity = new CaptchaEntity();
			captchaEntity.setCaptchaKey(captchaKey);
			captchaEntity.setInputStream(response.getEntity().getContent());
			return captchaEntity;
		} catch (ClientProtocolException e) {
			logger.error("Fetch:", e);
		} catch (IOException e) {
			logger.error("Fetch:", e);
		} catch (Exception e) {
			logger.error("Fetch:", e);
		}
		return null;
	}

	/**
	 * 检查验证码是否正确
	 * 
	 * @param key
	 * @param code
	 * @return
	 */
	public static boolean check(String key, String code) {
		try {
			Map<String, String> requestProperty = new HashMap<String, String>();
			requestProperty.put(BasicAuthBuilder.headerKey(),
					BasicAuthBuilder.headerValue());
			requestProperty.put(CAPTCHA_HEADER_KEY, key);
			Map<String, String> params = new HashMap<String, String>();
			params.put("captcha", code);
			if (HttpStatus.SC_OK == APIUtil.postResponseCodeFromServer(
					UrlBuilder.buildCaptchaFetcherUrl(), params,
					requestProperty)) {
				return true;
			}
		} catch (Exception e) {
			logger.error("Check:", e);
		}
		return false;
	}

	/**
	 * 校验用户
	 * 
	 * @param userEntity
	 * @return
	 */
	private static boolean validate(UserDomain userEntity) {
		if (userEntity == null) {
			return false;
		}
		if (StringUtil.isNull(userEntity.getIdentity())) {
			return false;
		}
		if (StringUtil.isNull(userEntity.getPassword())) {
			return false;
		}
		if (StringUtil.isNull(userEntity.getCaptcha())) {
			return false;
		}
		return true;
	}

	/**
	 * 校验邀请注册用户
	 * 
	 * @param inviteUserEntity
	 * @return
	 */
	private static boolean validate(InviteUserDomain inviteUserEntity) {
		if (inviteUserEntity == null) {
			return false;
		}
		if (StringUtil.isNull(inviteUserEntity.getMessage())) {
			return false;
		}
		if (inviteUserEntity.getInvitedUserList().size() < 1) {
			return false;
		} else {
			for (InvitedUser invitedUser : inviteUserEntity
					.getInvitedUserList()) {
				if (StringUtil.isNull(invitedUser.getIdentity())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 把user对象转换成json字符串
	 * 
	 * @param userEntity
	 * @return
	 */
	private static String convert2Json(UserDomain userEntity) {
		if (userEntity == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{\"identity\":\"");
		sb.append(userEntity.getIdentity());
		sb.append("\", \"password\":\"");
		sb.append(userEntity.getPassword());
		sb.append("\", \"name\":\"");
		sb.append(userEntity.getName());
		sb.append("\", \"enterprise\":");
		sb.append(userEntity.isEnterprise());
		sb.append(", \"captcha\":\"");
		sb.append(userEntity.getCaptcha());
		sb.append("\", \"returnTo\":\"");
		sb.append(userEntity.getReturnTo());
		sb.append("\", \"locale\":\"");
		sb.append(userEntity.getLocale());
		sb.append("\"}");
		return sb.toString();
	}
}
