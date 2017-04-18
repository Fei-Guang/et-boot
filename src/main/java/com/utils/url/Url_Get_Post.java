package com.utils.url;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.utils.FileUtil;
import com.utils.StringUtil;

/**
 * 以get或者post方式传递参数进行通信，由于该类中的方法对异常都进行了处理，所以对通信过程中传递参数的有效性需要采用标准约定来保证
 * 
 * @author liao.lh
 * 
 */
public class Url_Get_Post {

	private static Logger logger = LoggerFactory.getLogger(Url_Get_Post.class);

	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36";
	public static final int CONN_TIME_OUT = 30000;
	public static final int READ_TIME_OUT = 30000;

	/**
	 * 采用Get方式
	 * 
	 * @param urlStr
	 *            无参数的完整的url语句
	 * @param param
	 *            参数，格式为aaa=111&bbb=222.....
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 */
	public static String get(String urlStr, String param,
			boolean isLineSeparator) {
		return get(urlStr, param, "utf-8", null, "", null, isLineSeparator);
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
	 * @param prox
	 *            代理对象
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 */
	public static String get(String urlStr, String param, String encoding,
			String referer, String cookieVal, Proxy prox,
			boolean isLineSeparator) {
		Map<String, String> requestProperty = new HashMap<String, String>();
		if (!StringUtil.isNull(referer)) {
			requestProperty.put("Referer", referer);
		}
		if (!StringUtil.isNull(cookieVal)) {
			// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
			requestProperty.put("Cookie", cookieVal);
		}
		requestProperty.put("User-Agent", USER_AGENT);
		return get(urlStr, param, encoding, requestProperty, prox,
				isLineSeparator);
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
	 * @param prox
	 *            代理对象
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 */
	public static String get(String urlStr, String param, String encoding,
			Map<String, String> requestProperty, Proxy prox,
			boolean isLineSeparator) {
		StringBuffer result = new StringBuffer();
		BufferedReader in = null;
		try {
			URL url;
			if (StringUtil.isNull(param)) {
				url = new URL(urlStr);
			} else {
				url = new URL(urlStr + "?" + param);
			}
			URLConnection connection = null;
			if (prox != null) {
				connection = url.openConnection(prox);
			} else {
				connection = url.openConnection();
			}
			connection.setConnectTimeout(CONN_TIME_OUT);
			connection.setReadTimeout(READ_TIME_OUT);
			if (requestProperty != null) {
				Set<String> keys = requestProperty.keySet();
				for (String key : keys) {
					connection
							.setRequestProperty(key, requestProperty.get(key));
				}
			}
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), encoding));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
				if (isLineSeparator == true) {
					result.append(StringUtil.getLineSeparator());
				}
			}
		} catch (Exception e) {
			logger.error("Get:", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("Get:", e);
				}
			}
		}
		return result.toString();
	}

	/**
	 * 采用Post方式，以字符串形式发送参数
	 * 
	 * @param urlStr
	 *            无参数的完整的url语句
	 * @param param
	 *            参数，格式为aaa=111&bbb=222.....
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 */
	public static String postByString(String urlStr, String param,
			boolean isLineSeparator) {
		return postByString(urlStr, param, "utf-8", null, "", null,
				isLineSeparator);
	}

	/**
	 * 采用Post方式，以字符串形式发送参数
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
	 * @param prox
	 *            代理对象
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 */
	public static String postByString(String urlStr, String param,
			String encoding, String referer, String cookieVal, Proxy prox,
			boolean isLineSeparator) {
		Map<String, String> requestProperty = new HashMap<String, String>();
		if (!StringUtil.isNull(referer)) {
			requestProperty.put("Referer", referer);
		}
		if (!StringUtil.isNull(cookieVal)) {
			// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
			requestProperty.put("Cookie", cookieVal);
		}
		requestProperty.put("User-Agent", USER_AGENT);
		return postByString(urlStr, param, encoding, requestProperty, prox,
				isLineSeparator);
	}

	/**
	 * 采用Post方式，以字符串形式发送参数
	 * 
	 * @param urlStr
	 *            无参数的完整的url语句
	 * @param param
	 *            参数，格式为aaa=111&bbb=222.....
	 * @param encoding
	 *            编码
	 * @param requestProperty
	 *            请求头
	 * @param prox
	 *            代理对象
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 */
	public static String postByString(String urlStr, String param,
			String encoding, Map<String, String> requestProperty, Proxy prox,
			boolean isLineSeparator) {
		StringBuffer result = new StringBuffer();
		BufferedReader in = null;
		OutputStreamWriter out = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection httpConn = null;
			if (prox != null) {
				httpConn = (HttpURLConnection) url.openConnection(prox);
			} else {
				httpConn = (HttpURLConnection) url.openConnection();
			}
			httpConn.setConnectTimeout(CONN_TIME_OUT);
			httpConn.setReadTimeout(READ_TIME_OUT);
			if (requestProperty != null) {
				Set<String> keys = requestProperty.keySet();
				for (String key : keys) {
					httpConn.setRequestProperty(key, requestProperty.get(key));
				}
			}
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			out = new OutputStreamWriter(httpConn.getOutputStream(), encoding);
			out.write(param);
			out.flush();
			in = new BufferedReader(new InputStreamReader(
					httpConn.getInputStream(), encoding));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
				if (isLineSeparator == true) {
					result.append(StringUtil.getLineSeparator());
				}
			}
		} catch (Exception e) {
			logger.error("PostByString:", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("PostByString:", e);
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("PostByString:", e);
				}
			}
		}
		return result.toString();
	}

	/**
	 * 采用Post方式，以字节数组形式发送参数
	 * 
	 * @param urlStr
	 *            无参数的完整的url语句
	 * @param buffer
	 *            指定传送内容的Buffer
	 * @param len
	 *            指定缓冲区的大小
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 * 
	 */
	public static String postByBinary(String urlStr, byte[] buffer, int len,
			boolean isLineSeparator) {
		return postByBinary(urlStr, buffer, len, "utf-8", null, "", null,
				isLineSeparator);
	}

	/**
	 * 采用Post方式，以字节数组形式发送参数
	 * 
	 * @param urlStr
	 *            无参数的完整的url语句
	 * @param buffer
	 *            指定传送内容的Buffer
	 * @param len
	 *            指定缓冲区的大小
	 * @param encoding
	 *            编码
	 * @param referer
	 *            请求来源
	 * @param cookieVal
	 *            cookie值
	 * @param prox
	 *            代理对象
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 * 
	 */
	public static String postByBinary(String urlStr, byte[] buffer, int len,
			String encoding, String referer, String cookieVal, Proxy prox,
			boolean isLineSeparator) {
		Map<String, String> requestProperty = new HashMap<String, String>();
		if (!StringUtil.isNull(referer)) {
			requestProperty.put("Referer", referer);
		}
		if (!StringUtil.isNull(cookieVal)) {
			// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
			requestProperty.put("Cookie", cookieVal);
		}
		requestProperty.put("User-Agent", USER_AGENT);
		return postByBinary(urlStr, buffer, len, encoding, requestProperty,
				prox, isLineSeparator);
	}

	/**
	 * 采用Post方式，以字节数组形式发送参数
	 * 
	 * @param urlStr
	 *            无参数的完整的url语句
	 * @param buffer
	 *            指定传送内容的Buffer
	 * @param len
	 *            指定缓冲区的大小
	 * @param encoding
	 *            编码
	 * @param requestProperty
	 *            请求头
	 * @param prox
	 *            代理对象
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 */
	public static String postByBinary(String urlStr, byte[] buffer, int len,
			String encoding, Map<String, String> requestProperty, Proxy prox,
			boolean isLineSeparator) {
		StringBuffer result = new StringBuffer();
		BufferedOutputStream wr = null;
		BufferedReader rd = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = null;
			if (prox != null) {
				conn = (HttpURLConnection) url.openConnection(prox);
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setConnectTimeout(CONN_TIME_OUT);
			conn.setReadTimeout(READ_TIME_OUT);
			if (requestProperty != null) {
				Set<String> keys = requestProperty.keySet();
				for (String key : keys) {
					conn.setRequestProperty(key, requestProperty.get(key));
				}
			}
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			wr = new BufferedOutputStream(conn.getOutputStream());
			wr.write(buffer, 0, len);
			wr.flush();
			// Get the response
			rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), encoding));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
				if (isLineSeparator == true) {
					result.append(StringUtil.getLineSeparator());
				}
			}
		} catch (Exception e) {
			logger.error("PostByBinary:", e);
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					logger.error("PostByBinary:", e);
				}
			}
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException e) {
					logger.error("PostByBinary:", e);
				}
			}
		}
		return result.toString();
	}

	/**
	 * 模拟表单提交，可以起到上传文件的作用
	 * 
	 * @param urlStr
	 *            提交地址
	 * @param textMap
	 *            模拟表单提交的字段
	 * @param fileMap
	 *            模拟表单提交的文件
	 * @param encoding
	 *            编码格式
	 * @param referer
	 *            请求来源
	 * @param cookieVal
	 *            cookie值
	 * @param prox
	 *            代理对象
	 * @param isLineSeparator
	 *            指定从服务器端接收的数据是否每行要添加换行
	 * @return 返回Web Server的响应
	 */
	public static String formUpload(String urlStr, Map<String, String> textMap,
			Map<String, String> fileMap, String encoding, String referer,
			String cookieVal, Proxy prox, boolean isLineSeparator) {
		StringBuffer result = new StringBuffer();
		OutputStream out = null;
		BufferedReader reader = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection httpConn = null;
			if (prox != null) {
				httpConn = (HttpURLConnection) url.openConnection(prox);
			} else {
				httpConn = (HttpURLConnection) url.openConnection();
			}
			httpConn.setConnectTimeout(CONN_TIME_OUT);
			httpConn.setReadTimeout(READ_TIME_OUT);
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("Accept", "*/*");
			httpConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			httpConn.setRequestProperty("User-Agent", USER_AGENT);
			httpConn.setRequestProperty("Charset", encoding);
			if (!StringUtil.isNull(referer)) {
				httpConn.setRequestProperty("Referer", referer);
			}
			if (!StringUtil.isNull(cookieVal)) {
				// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
				httpConn.setRequestProperty("Cookie", cookieVal);
			}
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false); // post方式不推荐使用缓存
			// 模拟上传文件的逻辑代码
			// 设置边界
			String BOUNDARY = "----WebKitFormBoundary"
					+ System.currentTimeMillis();
			httpConn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);
			// 请求正文信息
			out = new DataOutputStream(httpConn.getOutputStream());
			// 第一部分，表单字段
			if (textMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator<Entry<String, String>> iter = textMap.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Entry<String, String> entry = (Entry<String, String>) iter
							.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}
			// 第二部分，表单文件
			if (fileMap != null) {
				Iterator<Entry<String, String>> iter = fileMap.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Entry<String, String> entry = (Entry<String, String>) iter
							.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
					String contentType = new MimetypesFileTypeMap()
							.getContentType(file);
					if (FileUtil.isImageFile(file)) {
						contentType = "image/" + FileUtil.getSuffix(file);
					} else if (FileUtil.isAudioFile(file)) {
						contentType = "audio/" + FileUtil.getSuffix(file);
					}
					if (StringUtil.isNull(contentType)) {
						contentType = "application/octet-stream";
					}
					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY)
							.append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"; filename=\"" + filename
							+ "\"\r\n");
					strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
					out.write(strBuf.toString().getBytes());
					DataInputStream in = null;
					try {
						in = new DataInputStream(new FileInputStream(file));
						int bytes = 0;
						byte[] bufferOut = new byte[1024];
						while ((bytes = in.read(bufferOut)) != -1) {
							out.write(bufferOut, 0, bytes);
						}
					} finally {
						if (in != null) {
							try {
								in.close();
							} catch (IOException e) {
								logger.error("FormUpload:", e);
							}
						}
					}
				}
			}
			// 第三部分，结尾部分
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			reader = new BufferedReader(new InputStreamReader(
					httpConn.getInputStream(), encoding));
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line);
				if (isLineSeparator == true) {
					result.append(StringUtil.getLineSeparator());
				}
			}
		} catch (Exception e) {
			logger.error("FormUpload:", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("FormUpload:", e);
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error("FormUpload:", e);
				}
			}
		}
		return result.toString();
	}

	/**
	 * 下载文件
	 * 
	 * @param targetURL
	 * @param localFilePath
	 * @param defaultName
	 * @param referer
	 * @param cookieVal
	 * @param prox
	 * @return
	 */
	public static boolean downloadFile(String targetURL, String localFilePath,
			String defaultName, String referer, String cookieVal, Proxy prox) {
		boolean result = true;
		DataInputStream is = null;
		FileOutputStream out = null;
		try {
			URL url = new URL(targetURL);
			HttpURLConnection httpConn = null;
			if (prox != null) {
				httpConn = (HttpURLConnection) url.openConnection(prox);
			} else {
				httpConn = (HttpURLConnection) url.openConnection();
			}
			httpConn.setConnectTimeout(CONN_TIME_OUT);
			httpConn.setReadTimeout(READ_TIME_OUT);
			httpConn.setRequestProperty("Connection", "Keep-Alive");
			httpConn.setRequestProperty("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			httpConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			httpConn.setRequestProperty("User-Agent", USER_AGENT);
			if (!StringUtil.isNull(referer)) {
				httpConn.setRequestProperty("Referer", referer);
			}
			if (!StringUtil.isNull(cookieVal)) {
				// 发送cookie信息上去，以表明自己的身份，否则会被认为没有权限
				httpConn.setRequestProperty("Cookie", cookieVal);
			}
			is = new DataInputStream(httpConn.getInputStream());
			Map<String, List<String>> headerFields = httpConn.getHeaderFields();
			List<String> headerValues = headerFields.get("Content-Disposition");
			if (headerValues != null) {
				for (String headerValue : headerValues) {
					if (headerValue.startsWith("attachment; filename=")) {
						try {
							String filename = new String(headerValue
									.substring(
											("attachment; filename=").length())
									.replace("\"", "").getBytes("iso-8859-1"),
									"utf-8");
							out = new FileOutputStream(localFilePath
									+ File.separator + filename);
						} catch (UnsupportedEncodingException ue) {

						}
					}
				}
			}
			if (out == null) {
				out = new FileOutputStream(localFilePath + File.separator
						+ defaultName);
			}
			byte[] buffer = new byte[1024 * 1000];
			int read = -1;
			while ((read = is.read(buffer)) != -1) {
				if (read > 0) {
					byte[] chunk = new byte[read];
					System.arraycopy(buffer, 0, chunk, 0, read);
					out.write(chunk);
				}
			}
		} catch (Exception e) {
			logger.error("DownloadFile:", e);
			result = false;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("DownloadFile:", e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("DownloadFile:", e);
				}
			}
		}
		return result;
	}

	/**
	 * 获取链接对应的域名
	 * 
	 * @param url
	 * @return
	 */
	public static String getDomain(String url) {
		int beginIndex = 0;
		if (url.startsWith("https://")) {
			beginIndex = 8;
		} else if (url.startsWith("http://")) {
			beginIndex = 7;
		}
		url = url.substring(beginIndex);
		int endIndex = url.indexOf("/");
		if (endIndex != -1) {
			return url.substring(0, endIndex);
		} else {
			return url;
		}
	}

	/**
	 * 获取链接对应的Cookie值
	 * 
	 * @param urlStr
	 * @param param
	 * @return
	 */
	public static String getCookieVal(String urlStr, String param) {
		StringBuffer result = new StringBuffer();
		OutputStreamWriter out = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			httpConn.setConnectTimeout(CONN_TIME_OUT);
			httpConn.setReadTimeout(READ_TIME_OUT);
			httpConn.setRequestProperty("User-Agent", USER_AGENT);
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setRequestMethod("POST");
			if (!StringUtil.isNull(param)) {
				out = new OutputStreamWriter(httpConn.getOutputStream(),
						"utf-8");
				out.write(param);
				out.flush();
			}
			List<String> cookies = httpConn.getHeaderFields().get("Set-Cookie");
			if (cookies != null) {
				for (String str : cookies) {
					if (!StringUtil.isNull(str)) {
						str = str.substring(0, str.indexOf(";") + 1);
					}
					result.append(str);
				}
			}
		} catch (Exception e) {
			logger.error("GetCookieVal:", e);
			return null;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("GetCookieVal:", e);
				}
			}
		}
		return result.toString();
	}

	/**
	 * 启动Cookie管理策略，执行该方法后，将每次访问的Cookie信息都保存起来，以便模拟登录
	 */
	public static void startCookieManager() {
		CookieHandler.setDefault(new CookieManager(null,
				CookiePolicy.ACCEPT_ALL));
	}

	/************************************ 主方法用于测试 *************************************/
	public static void main(String[] args) {
		// String cookie = getCookieVal(
		// "http://www.chinahr.com/modules/jsperson/login_ajax.php",
		// "uname=liaolinghao@126.com&password=liaolinghao&http_referer=&action=login&typeid=&remember=1"
		// );
		// System.out.println(cookie);
		startCookieManager();
		postByString(
				"http://www.chinahr.com/modules/jsperson/login_ajax.php",
				"uname=liaolinghao@126.com&password=liaolinghao&http_referer=&action=login&typeid=&remember=1",
				true);
		System.out
				.println(com.utils.FileUtil
						.newFile(
								"d:/1231.txt",
								get("http://www.chinahr.com/job/f055ae84a745085389bb01fbj.html",
										"", true)));

	}
}
