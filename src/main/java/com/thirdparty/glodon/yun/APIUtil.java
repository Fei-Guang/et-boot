package com.thirdparty.glodon.yun;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;

import com.thirdparty.glodon.yun.utils.BasicAuthBuilder;
import com.utils.url.HttpClient_Get_Post;

public class APIUtil {

	public static String getResponseFromServer(String url) {
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put(BasicAuthBuilder.headerKey(),
				BasicAuthBuilder.headerValue());
		return getResponseFromServer(url, requestProperty);
	}

	public static String getResponseFromServer(String url, String accessToken) {
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put(BasicAuthBuilder.headerKey(), "Bearer "
				+ accessToken);
		return getResponseFromServer(url, requestProperty);
	}

	public static String getResponseFromServer(String url,
			Map<String, String> requestProperty) {
		HttpClient_Get_Post httpClient_Get_Post = new HttpClient_Get_Post();
		String output = httpClient_Get_Post.doGet(url, null, null,
				requestProperty, true);
		httpClient_Get_Post.shutdown();
		return output;
	}

	public static int getResponseCodeFromServer(String url) throws Exception {
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put(BasicAuthBuilder.headerKey(),
				BasicAuthBuilder.headerValue());
		return getResponseCodeFromServer(url, requestProperty);
	}

	public static int getResponseCodeFromServer(String url, String accessToken)
			throws Exception {
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put(BasicAuthBuilder.headerKey(), "Bearer "
				+ accessToken);
		return getResponseCodeFromServer(url, requestProperty);
	}

	public static int getResponseCodeFromServer(String url,
			Map<String, String> requestProperty) throws Exception {
		HttpClient_Get_Post httpClient_Get_Post = new HttpClient_Get_Post();
		HttpResponse response = httpClient_Get_Post.doGet(url, null, null,
				requestProperty);
		int code = response.getStatusLine().getStatusCode();
		httpClient_Get_Post.shutdown();
		return code;
	}

	public static String postResponseFromServer(String url,
			Map<String, String> params) {
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put(BasicAuthBuilder.headerKey(),
				BasicAuthBuilder.headerValue());
		return postResponseFromServer(url, params, requestProperty);
	}

	public static String postResponseFromServer(String url,
			Map<String, String> params, String accessToken) {
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put(BasicAuthBuilder.headerKey(), "Bearer "
				+ accessToken);
		return postResponseFromServer(url, params, requestProperty);
	}

	public static String postResponseFromServer(String url,
			Map<String, String> params, Map<String, String> requestProperty) {
		HttpClient_Get_Post httpClient_Get_Post = new HttpClient_Get_Post();
		String output = httpClient_Get_Post.doPost(url, params, null,
				requestProperty, true);
		httpClient_Get_Post.shutdown();
		return output;
	}

	public static String postResponseFromServer(String url, String params) {
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put(BasicAuthBuilder.headerKey(),
				BasicAuthBuilder.headerValue());
		return postResponseFromServer(url, params, requestProperty);
	}

	public static String postResponseFromServer(String url, String params,
			String accessToken) {
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put(BasicAuthBuilder.headerKey(), "Bearer "
				+ accessToken);
		return postResponseFromServer(url, params, requestProperty);
	}

	public static String postResponseFromServer(String url, String params,
			Map<String, String> requestProperty) {
		HttpClient_Get_Post httpClient_Get_Post = new HttpClient_Get_Post();
		String output = httpClient_Get_Post.doPost(url, params,
				ContentType.APPLICATION_JSON, null, requestProperty, true);
		httpClient_Get_Post.shutdown();
		return output;
	}

	public static int postResponseCodeFromServer(String url,
			Map<String, String> params) throws Exception {
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put(BasicAuthBuilder.headerKey(),
				BasicAuthBuilder.headerValue());
		return postResponseCodeFromServer(url, params, requestProperty);
	}

	public static int postResponseCodeFromServer(String url,
			Map<String, String> params, String accessToken) throws Exception {
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put(BasicAuthBuilder.headerKey(), "Bearer "
				+ accessToken);
		return postResponseCodeFromServer(url, params, requestProperty);
	}

	public static int postResponseCodeFromServer(String url,
			Map<String, String> params, Map<String, String> requestProperty)
			throws Exception {
		HttpClient_Get_Post httpClient_Get_Post = new HttpClient_Get_Post();
		HttpResponse response = httpClient_Get_Post.doPost(url, params, null,
				requestProperty);
		int code = response.getStatusLine().getStatusCode();
		httpClient_Get_Post.shutdown();
		return code;
	}

	public static int postResponseCodeFromServer(String url, String params)
			throws Exception {
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put(BasicAuthBuilder.headerKey(),
				BasicAuthBuilder.headerValue());
		return postResponseCodeFromServer(url, params, requestProperty);
	}

	public static int postResponseCodeFromServer(String url, String params,
			String accessToken) throws Exception {
		Map<String, String> requestProperty = new HashMap<String, String>();
		requestProperty.put(BasicAuthBuilder.headerKey(), "Bearer "
				+ accessToken);
		return postResponseCodeFromServer(url, params, requestProperty);
	}

	public static int postResponseCodeFromServer(String url, String params,
			Map<String, String> requestProperty) throws Exception {
		HttpClient_Get_Post httpClient_Get_Post = new HttpClient_Get_Post();
		HttpResponse response = httpClient_Get_Post.doPost(url, params,
				ContentType.APPLICATION_JSON, null, requestProperty);
		int code = response.getStatusLine().getStatusCode();
		httpClient_Get_Post.shutdown();
		return code;
	}

}
