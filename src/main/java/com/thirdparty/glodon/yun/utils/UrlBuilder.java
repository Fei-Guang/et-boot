package com.thirdparty.glodon.yun.utils;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import com.thirdparty.glodon.yun.common.Constants;
import com.utils.Coder;

/**
 * OAuth API地址拼装
 * 
 * @author bigbird 2017-2-11
 */
public class UrlBuilder {

	private static final String LOGIN_REDIRECT_URL = "{0}/tokenLogin?token={1}&target_url={2}";
	private static final String ACCESS_TOKEN_URL = "{0}/oauth2/token?grant_type={1}";
	private static final String CAS_REDIRECT_URL = "{0}/serviceLogin?service_key={1}&callback_url={2}";
	private static final String CAS_MINI_REDIRECT_URL = "{0}/serviceLogin?Device&service_key={1}&device_css={2}&callback_url={3}";
	private static final String CAS_VALIDATE_URL = "{0}/cas/serviceValidate?ticket={1}&service={2}";
	private static final String CAS_LOGOUT_URL = "{0}/cas/serviceLogout?service={1}&return_to={2}";

	// 广联云API
	private static final String MODIFY_PASSWORD_URL = "{0}/password?access_token={1}";
	private static final String LOGIN_TOKEN_URL = "{0}/login_token";
	private static final String GET_USER_URL = "{0}/userinfo";
	private static final String NAME_CHECK_URL = "{0}/identity?id={1}";
	private static final String CAPTCHA_FETCHER_URL = "{0}/captcha";
	private static final String SIGN_UP_URL = "{0}/signup";
	private static final String INVITE_URL = "{0}/invite";
	private static final String RESET_PASSWORD_URL = "{0}/reset_password";
	private static final String BIND_MOBILE_URL = "{0}/bind/mobile";
	private static final String SMS_CODE_FETCHER_URL = "{0}/code?captcha={1}&mobile={2}&type={3}";
	// 广联云3.0API
	private static final String GET_USER_ID_URL = "{0}/security/userid?identity={1}";
	private static final String USER_FETCHER_URL = "{0}/security/userinfobyid?identity={1}";
	private static final String USER_LOGIN_URL = "{0}/oauth2/login";

	/**
	 * 拼装本地应用的路径
	 * 
	 * @param request
	 * @return
	 */
	public static String buildLocalAppUrl(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append(request.getScheme());
		sb.append("://");
		String server = request.getServerName();
		// etender serving at localhost behind ngix in production
		if ( server.equals("127.0.0.1") )
			server = "etender.glodon.com.sg";
		sb.append(server);
		if (80 != request.getServerPort()) {
			sb.append(":");
			sb.append(request.getServerPort());
		}
		sb.append(request.getContextPath());
		return sb.toString();
	}

	/**
	 * 拼装CAS服务器的跳转地址
	 * 
	 * @param ticket
	 * @return
	 */
	public static String buildCasRedirectUrl(HttpServletRequest request) {
		return MessageFormat.format(CAS_REDIRECT_URL,
				Constants.ACCOUNT_SERVER_URL, Constants.SERVICE_KEY,
				Coder.URLEncoderUTF8(Constants.SSO_CAS_URL));
	}

	/**
	 * 拼装CAS服务器的跳转地址(小页面)
	 * 
	 * @param request
	 * @return
	 */
	public static String buildCasMiniRedirectUrl(HttpServletRequest request) {
		String localAppUrl = buildLocalAppUrl(request);
		return MessageFormat.format(CAS_MINI_REDIRECT_URL,
				Constants.ACCOUNT_SERVER_URL, Constants.SERVICE_KEY,
				Constants.MINI_CSS_URL, localAppUrl + "/login/callback");
	}

	/**
	 * 拼装验证和获取登录用户的后台API地址
	 * 
	 * @param ticket
	 * @return
	 */
	public static String buildCasValidateUrl(String ticket) {
		return MessageFormat.format(CAS_VALIDATE_URL,
				Constants.ACCOUNT_SERVER_URL, ticket, Constants.SERVICE_KEY);
	}

	/**
	 * 拼装CAS单点登出的URL
	 * 
	 * @param returnUrl
	 * @return
	 */
	public static String buildLogoutUrl(HttpServletRequest request) {
		String localAppUrl = buildLocalAppUrl(request);
		return MessageFormat.format(CAS_LOGOUT_URL,
				Constants.ACCOUNT_SERVER_URL, Constants.SERVICE_KEY,
				localAppUrl + "/login/logout");
	}

	/**
	 * 拼装用户凭证方式获取token的URL
	 * 
	 * @return
	 */
	public static String buildGrantByPasswordUrl() {
		return MessageFormat.format(ACCESS_TOKEN_URL,
				Constants.ACCOUNT_SERVER_URL, "password");
	}

	/**
	 * 拼装Login跳转到account的URL
	 * 
	 * @return
	 */
	public static String buildLoginRedirectUrl(String login_token) {
		return MessageFormat.format(LOGIN_REDIRECT_URL,
				Constants.ACCOUNT_SERVER_URL, login_token,
				Coder.URLEncoderUTF8(Constants.LOCAL_APP_URL));
	}

	/**
	 * 拼装修改密码的API路径
	 * 
	 * @param accessToken
	 * @return
	 */
	public static String buildModifyPasswordUrl(String accessToken) {
		return MessageFormat.format(MODIFY_PASSWORD_URL,
				Constants.API_ACCOUNT_URL, accessToken);
	}

	/**
	 * 拼装Login获取login_Token的跳转account的URL
	 * 
	 * @return
	 */
	public static String buildLoginTokenUrl() {
		return MessageFormat.format(LOGIN_TOKEN_URL, Constants.API_ACCOUNT_URL);
	}

	/**
	 * 拼装获取当前登录用户对应信息的URL
	 * 
	 * @return
	 */
	public static String buildGetUserUrl() {
		return MessageFormat.format(GET_USER_URL, Constants.API_ACCOUNT_URL);
	}

	/**
	 * 拼装检查用户名的URL
	 * 
	 * @param name
	 * @return
	 */
	public static String buildNameCheckerUrl(String name) {
		return MessageFormat.format(NAME_CHECK_URL, Constants.API_ACCOUNT_URL,
				name);
	}

	/**
	 * 拼装获取图片验证码的URL
	 * 
	 * @return
	 */
	public static String buildCaptchaFetcherUrl() {
		return MessageFormat.format(CAPTCHA_FETCHER_URL,
				Constants.API_ACCOUNT_URL);
	}

	/**
	 * 拼装发送短信验证码的URL
	 * 
	 * @param captcha
	 * @param mobile
	 * @param type
	 *            手机验证码类型（signup，bind，reset或login）默认是signup
	 * @return
	 */
	public static String buildSmsCodeFetcherUrl(String captcha, String mobile,
			String type) {
		return MessageFormat.format(SMS_CODE_FETCHER_URL,
				Constants.API_ACCOUNT_URL, captcha, mobile, type);
	}

	/**
	 * 拼装用户注册的URL
	 * 
	 * @return
	 */
	public static String buildSignupUrl() {
		return MessageFormat.format(SIGN_UP_URL, Constants.API_ACCOUNT_URL);
	}

	/**
	 * 拼装邀请用户注册的URL
	 * 
	 * @return
	 */
	public static String buildInviteUrl() {
		return MessageFormat.format(INVITE_URL, Constants.API_ACCOUNT_URL);
	}

	/**
	 * 拼装重置密码的URL
	 * 
	 * @return
	 */
	public static String buildResetPasswordUrl() {
		return MessageFormat.format(RESET_PASSWORD_URL,
				Constants.API_ACCOUNT_URL);
	}

	/**
	 * 拼装绑定手机URL
	 * 
	 * @return
	 */
	public static String buildBindMobileUrl() {
		return MessageFormat.format(BIND_MOBILE_URL, Constants.API_ACCOUNT_URL);
	}

	/**
	 * 拼装获取用户id的URL
	 * 
	 * @param identity
	 * @return
	 */
	public static String buildGetUserIDUrl(String identity) {
		return MessageFormat.format(GET_USER_ID_URL,
				Constants.API_V3_ACCOUNT_URL, Coder.URLEncoderUTF8(identity));
	}

	/**
	 * 拼装根据用户名获取用户信息的URL
	 * 
	 * @param name
	 * @return
	 */
	public static String buildUserFetcherUrl(String identity) {
		return MessageFormat.format(USER_FETCHER_URL,
				Constants.API_V3_ACCOUNT_URL, identity);
	}

	/**
	 * 拼装V3版本用户登录接口
	 * 
	 * @return
	 */
	public static String buildUserLoginUrl() {
		return MessageFormat.format(USER_LOGIN_URL,
				Constants.API_V3_ACCOUNT_URL);
	}

}
