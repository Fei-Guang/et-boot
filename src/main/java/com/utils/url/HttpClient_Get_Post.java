package com.utils.url;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.utils.StringUtil;
import com.utils.url.proxy.HttpProxy;

/**
 * 创建一个多线程环境下面可用的httpClient，以get或者post方式传递参数进行通信，采用HttpClient实现，
 * 由于该类中的方法对异常都进行了处理， 所以对通信过程中传递参数的有效性需要采用标准约定来保证
 * 
 * @author liao.lh
 * 
 */
public class HttpClient_Get_Post {

	private static Logger logger = LoggerFactory
			.getLogger(HttpClient_Get_Post.class);

	private int CONN_TIME_OUT = 30000;
	private int READ_TIME_OUT = 30000;

	private HttpProxy httpProxy;

	private CloseableHttpClient httpClient;

	public HttpClient_Get_Post() {

	}

	public HttpClient_Get_Post(HttpProxy httpProxy) {
		this.httpProxy = httpProxy;
	}

	public HttpClient_Get_Post(int TIME_OUT) {
		this(TIME_OUT, null);
	}

	public HttpClient_Get_Post(int TIME_OUT, HttpProxy httpProxy) {
		this.CONN_TIME_OUT = TIME_OUT;
		this.READ_TIME_OUT = TIME_OUT;
		this.httpProxy = httpProxy;
	}

	public HttpClient_Get_Post(int CONN_TIME_OUT, int READ_TIME_OUT) {
		this(CONN_TIME_OUT, READ_TIME_OUT, null);
	}

	public HttpClient_Get_Post(int CONN_TIME_OUT, int READ_TIME_OUT,
			HttpProxy httpProxy) {
		this.CONN_TIME_OUT = CONN_TIME_OUT;
		this.READ_TIME_OUT = READ_TIME_OUT;
		this.httpProxy = httpProxy;
	}

	/**
	 * 采用Get方式
	 * 
	 * @param urlStr
	 *            无参数的完整的url语句
	 * @param param
	 *            参数，格式为aaa=111&bbb=222.....
	 * @param encoding
	 *            编码
	 * @param referer
	 *            请求来源
	 * @param cookieVal
	 *            cookie值
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public String doGet(String urlStr, String param, String encoding,
			String referer, String cookieVal, boolean isLineSeparator) {
		Map<String, String> requestProperty = new HashMap<String, String>();
		if (!StringUtil.isNull(referer)) {
			requestProperty.put("Referer", referer);
		}
		if (!StringUtil.isNull(cookieVal)) {
			// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
			requestProperty.put("Cookie", cookieVal);
		}
		return doGet(urlStr, param, encoding, requestProperty, isLineSeparator);
	}

	/**
	 * 采用Get方式
	 * 
	 * @param urlStr
	 *            无参数的完整的url语句
	 * @param param
	 *            参数，格式为aaa=111&bbb=222.....
	 * @param encoding
	 *            编码
	 * @param requestProperty
	 *            请求头
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return
	 */
	public String doGet(String urlStr, String param, String encoding,
			Map<String, String> requestProperty, boolean isLineSeparator) {
		try {
			// 得到返回的response.
			HttpResponse response = doGet(urlStr, param, encoding,
					requestProperty);
			Header[] headers = response.getHeaders("Content-Encoding");
			boolean isGzip = false;
			for (Header h : headers) {
				if (h.getValue().equals("gzip")) {
					// 返回头中含有gzip
					isGzip = true;
					break;
				}
			}
			// 得到返回的client里面的实体对象信息.
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				if (isGzip) {
					return EntityUtils.toString(new GzipDecompressingEntity(
							entity));
				} else {
					return EntityUtils.toString(entity);
				}
			}
		} catch (Exception e) {
			logger.error("DoGet:", e);
		}
		return null;
	}

	/**
	 * 采用Get方式
	 * 
	 * @param urlStr
	 *            无参数的完整的url语句
	 * @param param
	 *            参数，格式为aaa=111&bbb=222.....
	 * @param encoding
	 *            编码
	 * @param requestProperty
	 *            请求头
	 * @return
	 */
	public HttpResponse doGet(String urlStr, String param, String encoding,
			Map<String, String> requestProperty) {
		if (httpClient == null) {
			httpClient = createHttpClient();
		}
		try {
			String url = StringUtil.isNull(param) ? urlStr : urlStr + "?"
					+ param;
			// 设置为get取连接的方式.
			HttpGet get = new HttpGet(url);
			RequestConfig requestConfig = loadRequestConfig();
			get.setConfig(requestConfig);
			if (requestProperty != null) {
				Set<String> keys = requestProperty.keySet();
				for (String key : keys) {
					get.addHeader(key, requestProperty.get(key));
				}
			}
			HttpContext httpContext = loadHttpContext();
			// 得到返回的response.
			return httpClient.execute(get, httpContext);
		} catch (Exception e) {
			logger.error("DoGet:", e);
		}
		return null;
	}

	/**
	 * 采用Post方式
	 * 
	 * @param url
	 *            无参数的完整的url语句
	 * @param params
	 *            参数
	 * @param encoding
	 *            编码
	 * @param referer
	 *            请求来源
	 * @param cookieVal
	 *            cookie值
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 */
	public String doPost(String url, Map<String, String> params,
			String encoding, String referer, String cookieVal,
			boolean isLineSeparator) {
		Map<String, String> requestProperty = new HashMap<String, String>();
		if (!StringUtil.isNull(referer)) {
			requestProperty.put("Referer", referer);
		}
		if (!StringUtil.isNull(cookieVal)) {
			// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
			requestProperty.put("Cookie", cookieVal);
		}
		return doPost(url, params, encoding, requestProperty, isLineSeparator);
	}

	/**
	 * 采用Post方式
	 * 
	 * @param url
	 *            无参数的完整的url语句
	 * @param params
	 *            参数
	 * @param encoding
	 *            编码
	 * @param requestProperty
	 *            请求头
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return
	 */
	public String doPost(String url, Map<String, String> params,
			String encoding, Map<String, String> requestProperty,
			boolean isLineSeparator) {
		try {
			HttpResponse response = doPost(url, params, encoding,
					requestProperty);
			Header[] headers = response.getHeaders("Content-Encoding");
			boolean isGzip = false;
			for (Header h : headers) {
				if (h.getValue().equals("gzip")) {
					// 返回头中含有gzip
					isGzip = true;
					break;
				}
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				if (isGzip) {
					return EntityUtils.toString(new GzipDecompressingEntity(
							entity));
				} else {
					return EntityUtils.toString(entity);
				}
			}
		} catch (Exception e) {
			logger.error("DoPost:", e);
		}
		return null;
	}

	/**
	 * 采用Post方式
	 * 
	 * @param url
	 *            无参数的完整的url语句
	 * @param params
	 *            参数
	 * @param encoding
	 *            编码
	 * @param requestProperty
	 *            请求头
	 * @return
	 */
	public HttpResponse doPost(String url, Map<String, String> params,
			String encoding, Map<String, String> requestProperty) {
		if (httpClient == null) {
			httpClient = createHttpClient();
		}
		try {
			HttpPost httpost = new HttpPost(url);
			RequestConfig requestConfig = loadRequestConfig();
			httpost.setConfig(requestConfig);
			// 添加参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			if (params != null && params.keySet().size() > 0) {
				Iterator<Entry<String, String>> iterator = params.entrySet()
						.iterator();
				while (iterator.hasNext()) {
					Entry<String, String> entry = iterator.next();
					nvps.add(new BasicNameValuePair(entry.getKey(), entry
							.getValue()));
				}
			}
			httpost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
			if (requestProperty != null) {
				Set<String> keys = requestProperty.keySet();
				for (String key : keys) {
					httpost.addHeader(key, requestProperty.get(key));
				}
			}
			HttpContext httpContext = loadHttpContext();
			return httpClient.execute(httpost, httpContext);
		} catch (Exception e) {
			logger.error("DoPost:", e);
		}
		return null;
	}

	/**
	 * 采用Post方式提交文本类型参数
	 * 
	 * @param url
	 *            无参数的完整的url语句
	 * @param param
	 *            参数
	 * @param encoding
	 *            编码
	 * @param referer
	 *            请求来源
	 * @param cookieVal
	 *            cookie值
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 */
	public String doPost(String url, String param, String encoding,
			String referer, String cookieVal, boolean isLineSeparator) {
		return doPost(url, param, ContentType.DEFAULT_TEXT, encoding, referer,
				cookieVal, isLineSeparator);
	}

	/**
	 * 采用Post方式提交文本类型参数
	 * 
	 * @param url
	 *            无参数的完整的url语句
	 * @param param
	 *            参数
	 * @param type
	 *            文本参数格式类型
	 * @param encoding
	 *            编码
	 * @param referer
	 *            请求来源
	 * @param cookieVal
	 *            cookie值
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 */
	public String doPost(String url, String param, ContentType type,
			String encoding, String referer, String cookieVal,
			boolean isLineSeparator) {
		Map<String, String> requestProperty = new HashMap<String, String>();
		if (!StringUtil.isNull(referer)) {
			requestProperty.put("Referer", referer);
		}
		if (!StringUtil.isNull(cookieVal)) {
			// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
			requestProperty.put("Cookie", cookieVal);
		}
		return doPost(url, param, type, encoding, requestProperty,
				isLineSeparator);
	}

	/**
	 * 采用Post方式提交文本类型参数
	 * 
	 * @param url
	 *            无参数的完整的url语句
	 * @param param
	 *            参数
	 * @param encoding
	 *            编码
	 * @param requestProperty
	 *            请求头
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return
	 */
	public String doPost(String url, String param, String encoding,
			Map<String, String> requestProperty, boolean isLineSeparator) {
		return doPost(url, param, ContentType.DEFAULT_TEXT, encoding,
				requestProperty, isLineSeparator);
	}

	/**
	 * 采用Post方式提交文本类型参数
	 * 
	 * @param url
	 *            无参数的完整的url语句
	 * @param param
	 *            参数
	 * @param type
	 *            文本参数格式类型
	 * @param encoding
	 *            编码
	 * @param requestProperty
	 *            请求头
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return
	 */
	public String doPost(String url, String param, ContentType type,
			String encoding, Map<String, String> requestProperty,
			boolean isLineSeparator) {
		try {
			HttpResponse response = doPost(url, param, type, encoding,
					requestProperty);
			Header[] headers = response.getHeaders("Content-Encoding");
			boolean isGzip = false;
			for (Header h : headers) {
				if (h.getValue().equals("gzip")) {
					// 返回头中含有gzip
					isGzip = true;
					break;
				}
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				if (isGzip) {
					return EntityUtils.toString(new GzipDecompressingEntity(
							entity));
				} else {
					return EntityUtils.toString(entity);
				}
			}
		} catch (Exception e) {
			logger.error("DoPost:", e);
		}
		return null;
	}

	/**
	 * 采用Post方式提交文本类型参数
	 * 
	 * @param url
	 *            无参数的完整的url语句
	 * @param param
	 *            参数
	 * @param encoding
	 *            编码
	 * @param requestProperty
	 *            请求头
	 * @return
	 */
	public HttpResponse doPost(String url, String param, String encoding,
			Map<String, String> requestProperty) {
		return doPost(url, param, ContentType.DEFAULT_TEXT, encoding,
				requestProperty);
	}

	/**
	 * 采用Post方式提交文本类型参数
	 * 
	 * @param url
	 *            无参数的完整的url语句
	 * @param param
	 *            参数
	 * @param type
	 *            文本参数格式类型
	 * @param encoding
	 *            编码
	 * @param requestProperty
	 *            请求头
	 * @return
	 */
	public HttpResponse doPost(String url, String param, ContentType type,
			String encoding, Map<String, String> requestProperty) {
		if (httpClient == null) {
			httpClient = createHttpClient();
		}
		try {
			HttpPost httpost = new HttpPost(url);
			RequestConfig requestConfig = loadRequestConfig();
			httpost.setConfig(requestConfig);
			if (requestProperty != null) {
				Set<String> keys = requestProperty.keySet();
				for (String key : keys) {
					httpost.addHeader(key, requestProperty.get(key));
				}
			}
			if (!StringUtil.isNull(param)) {
				StringEntity entity = new StringEntity(param, type);
				httpost.setEntity(entity);
			}
			HttpContext httpContext = loadHttpContext();
			return httpClient.execute(httpost, httpContext);
		} catch (Exception e) {
			logger.error("DoPost:", e);
		}
		return null;
	}

	/**
	 * 下载文件
	 * 
	 * @param targetURL
	 *            网络链接
	 * @param localFilePath
	 *            本地文件路径
	 */
	public boolean downloadFile(String targetURL, String localFilePath) {
		boolean result = true;
		if (httpClient == null) {
			httpClient = createHttpClient();
		}
		try {
			ResponseHandler<byte[]> handler = new ResponseHandler<byte[]>() {
				public byte[] handleResponse(HttpResponse response)
						throws ClientProtocolException, IOException {
					HttpEntity entity = response.getEntity();
					if (entity != null) {
						return EntityUtils.toByteArray(entity);
					} else {
						return null;
					}
				}
			};
			HttpGet get = new HttpGet(targetURL);
			RequestConfig requestConfig = loadRequestConfig();
			get.setConfig(requestConfig);
			HttpContext httpContext = loadHttpContext();
			byte[] charts = httpClient.execute(get, handler, httpContext);
			FileOutputStream out = new FileOutputStream(localFilePath);
			out.write(charts);
			out.close();
		} catch (Exception e) {
			logger.error("DownloadFile:", e);
			result = false;
		}
		return result;
	}

	/**
	 * 上传文件，模拟一个http的表单提交请求上传文件，但是中文名称文件有可能出问题，该方法未经过有效性测试
	 * 
	 * @param targetURL
	 *            网络链接
	 * @param localFilePath
	 *            本地文件路径
	 * 
	 * @return
	 */
	public boolean uploadFile(String targetURL, String localFilePath) {
		boolean result = true;
		if (httpClient == null) {
			httpClient = createHttpClient();
		}
		try {
			HttpPost httpost = new HttpPost(targetURL);
			RequestConfig requestConfig = loadRequestConfig();
			httpost.setConfig(requestConfig);
			File localFile = new File(localFilePath);
			FileEntity reqEntity = new FileEntity(localFile);
			httpost.setEntity(reqEntity);
			HttpContext httpContext = loadHttpContext();
			HttpResponse response = httpClient.execute(httpost, httpContext);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					// 显示服务器返回的响应内容
					logger.info(EntityUtils.toString(entity));
					EntityUtils.consume(entity);
				}
			} else {
				result = false;
			}
		} catch (Exception e) {
			logger.error("UploadFile:", e);
			result = false;
		}
		return result;
	}

	/**
	 * 获取请求配置信息
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private RequestConfig loadRequestConfig() {
		if (httpProxy == null) {
			return RequestConfig.custom().setSocketTimeout(CONN_TIME_OUT)
					.setConnectTimeout(CONN_TIME_OUT)
					.setConnectionRequestTimeout(READ_TIME_OUT)
					.setStaleConnectionCheckEnabled(true).build();
		} else {
			// 依次是代理地址，代理端口号，协议类型
			HttpHost proxy = new HttpHost(httpProxy.getHost(),
					httpProxy.getPort(), "http");
			if (!StringUtil.isNull(httpProxy.getUser())
					&& !StringUtil.isNull(httpProxy.getPassword())) {
				return RequestConfig.custom().setSocketTimeout(CONN_TIME_OUT)
						.setConnectTimeout(CONN_TIME_OUT)
						.setConnectionRequestTimeout(READ_TIME_OUT)
						.setStaleConnectionCheckEnabled(true).setProxy(proxy)
						.setAuthenticationEnabled(true).build();
			} else {
				return RequestConfig.custom().setSocketTimeout(CONN_TIME_OUT)
						.setConnectTimeout(CONN_TIME_OUT)
						.setConnectionRequestTimeout(READ_TIME_OUT)
						.setStaleConnectionCheckEnabled(true).setProxy(proxy)
						.build();
			}
		}
	}

	/**
	 * 获取请求执行上下文
	 * 
	 * @return
	 */
	private HttpContext loadHttpContext() {
		if (httpProxy != null) {
			if (!StringUtil.isNull(httpProxy.getUser())
					&& !StringUtil.isNull(httpProxy.getPassword())) {
				HttpContext httpContext = new BasicHttpContext();
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
				credsProvider.setCredentials(new AuthScope(httpProxy.getHost(),
						httpProxy.getPort()), new UsernamePasswordCredentials(
						httpProxy.getUser(), httpProxy.getPassword()));
				httpContext.setAttribute(HttpClientContext.CREDS_PROVIDER,
						credsProvider);
				return httpContext;
			}
		}
		return null;
	}

	/**
	 * 关闭浏览器
	 */
	public void shutdown() {
		if (httpClient != null) {
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("Shutdown:", e);
			}
		}
	}

	public CloseableHttpClient createHttpClient() {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		return httpClient;
	}

	/************************************ 主方法用于测试 *************************************/
	public static void main(String[] args) {
		HttpClient_Get_Post httpClient_Get_Post = new HttpClient_Get_Post();
		httpClient_Get_Post
				.downloadFile(
						"http://image.58.com/showphone.aspx?t=v55&v=A0658738F423128A67979B956895273C6",
						"e:/test.jpg");
	}

}
