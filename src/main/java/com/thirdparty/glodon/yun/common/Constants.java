package com.thirdparty.glodon.yun.common;

/**
 * 常量
 * 
 * @author zhouxing 2014-03-26
 */
public class Constants {

	public static final int CONN_TIME_OUT = 30000;
	public static final int READ_TIME_OUT = 30000;

	public static final String LOCAL_APP_URL = "http://127.0.0.1:8080/pages/protected/success.jsp";
	public static final String SSO_CAS_URL = "http://127.0.0.1:8080/pages/sso_cas.jsp";

	public static String SERVICE_KEY = "3dh4xA04Ge7Crf8IJ5zMg2RX2wWXr473";
	public static String SERVER_SECRET = "qSkj24PIIUsjqzKNixUYFcIiemnzK6RM";

	public static String PROTOCOL = "https";

	public static final String MINI_CSS_URL = PROTOCOL
			+ "://static.glodon.com/static/resources/portal/account.login.css";

	public static final String ACCOUNT_SERVER_URL = PROTOCOL
			+ "://account.glodon.com";

	public static final String API_OAUTH_GET_TOKEN = ACCOUNT_SERVER_URL
			+ "/oauth2/token";

	public static final String API_ACCOUNT_URL = ACCOUNT_SERVER_URL + "/api";

	public static final String API_V3_ACCOUNT_URL = ACCOUNT_SERVER_URL
			+ "/v3/api";
}
