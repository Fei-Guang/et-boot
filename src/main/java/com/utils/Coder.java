package com.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.BitSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import sun.misc.BASE64Decoder;

import java.util.Base64;


import com.utils.arithmetic.MD5;

/**
 * 编码类
 * 
 * @author liao.lh
 * 
 */
public class Coder {

	private static Logger logger = LoggerFactory.getLogger(Coder.class);

	public static final String DEFAULT_ENCODING = "UTF-8";

	private static BitSet dontNeedEncoding;

	private static final int caseDiff = ('a' - 'A');

	static {
		dontNeedEncoding = new BitSet(256);
		int i;
		for (i = 'a'; i <= 'z'; i++) {
			dontNeedEncoding.set(i);
		}
		for (i = 'A'; i <= 'Z'; i++) {
			dontNeedEncoding.set(i);
		}
		for (i = '0'; i <= '9'; i++) {
			dontNeedEncoding.set(i);
		}
		dontNeedEncoding.set('-');
		dontNeedEncoding.set('_');
		dontNeedEncoding.set('.');
		dontNeedEncoding.set('*');
	}

	private Coder() {

	}

	/**
	 * 将字符串进行base64编码
	 * 
	 * @param s
	 * @return
	 */
	public static String encodeBase64(String s) {
		if (s == null || s.equals("")) {
			return s;
		} else {
			try {
				
				return encodeBase64(s.getBytes(DEFAULT_ENCODING));
			} catch (Exception e) {
				return encodeBase64(s.getBytes());
			}
		}
	}

	/**
	 * 将字节数组以Base64方式编码为字符串
	 * 
	 * @param bytes
	 *            待编码的byte数组
	 * @return 编码后的字符串
	 * */
	public static String encodeBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
		//return new BASE64Encoder().encode(bytes);
	}

	/**
	 * 将BASE64 编码的字符串进行解码
	 * 
	 * @param encodeStr
	 *            待解码的字符串
	 * @return
	 */
	public static String decodeBase64(String encodeStr) {
		if (encodeStr == null || encodeStr.equals("")) {
			return encodeStr;
		}
		try {
			byte[] b = decodeBase64ToByteArray(encodeStr);
			if (b != null) {
				return new String(b, DEFAULT_ENCODING);
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将以Base64方式编码的字符串解码为字节数组
	 * 
	 * @param encodeStr
	 *            待解码的字符串
	 * @return 解码后的字节数组
	 * */
	public static byte[] decodeBase64ToByteArray(String encodeStr) {
			byte[] bt = Base64.getDecoder().decode(encodeStr);
			return bt;		
	}

	/**
	 * 该方法相比URLEncoder.encode(s,"utf-8")的区别在于对空格，该方法使用20
	 * %替换，而URLEncoder.encode(s,"utf-8")使用+号
	 * 
	 * @param s
	 * @return
	 */
	public static String encodeUTF8(String s) {
		try {
			return encode(s, "UTF-8");
		} catch (UnsupportedEncodingException exp) {
			throw new RuntimeException("Unsupported UTF-8 Encoding");
		}
	}

	/**
	 * 采用指定编码格式对字符串进行编码
	 * 
	 * @param s
	 * @param enc
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String s, String enc)
			throws UnsupportedEncodingException {

		boolean needToChange = false;
		boolean wroteUnencodedChar = false;
		int maxBytesPerChar = 10;
		StringBuffer out = new StringBuffer(s.length());
		ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);

		OutputStreamWriter writer = new OutputStreamWriter(buf, enc);

		for (int i = 0; i < s.length(); i++) {
			int c = s.charAt(i);
			if (dontNeedEncoding.get(c)) {
				if (c == ' ') {
					c = '+';
					needToChange = true;
				}
				out.append((char) c);
				wroteUnencodedChar = true;
			} else {
				try {
					if (wroteUnencodedChar) {
						writer = new OutputStreamWriter(buf, enc);
						wroteUnencodedChar = false;
					}
					writer.write(c);
					if (c >= 0xD800 && c <= 0xDBFF) {
						if ((i + 1) < s.length()) {
							int d = s.charAt(i + 1);
							if (d >= 0xDC00 && d <= 0xDFFF) {
								writer.write(d);
								i++;
							}
						}
					}
					writer.flush();
				} catch (IOException e) {
					buf.reset();
					continue;
				}
				byte[] ba = buf.toByteArray();
				for (int j = 0; j < ba.length; j++) {
					out.append('%');
					char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
					if (Character.isLetter(ch)) {
						ch -= caseDiff;
					}
					out.append(ch);
					ch = Character.forDigit(ba[j] & 0xF, 16);
					if (Character.isLetter(ch)) {
						ch -= caseDiff;
					}
					out.append(ch);
				}
				buf.reset();
				needToChange = true;
			}
		}

		return (needToChange ? out.toString() : s);
	}

	/**
	 * 使用URLEncoder的UTF-8编码方法
	 * 
	 * @param s
	 * @return
	 */
	public static String URLEncoderUTF8(String s) {
		try {
			return URLEncoder.encode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported UTF-8 Encoding");
		}
	}

	/**
	 * 使用URLDecoder的UTF-8解码方法
	 * 
	 * @param s
	 * @return
	 */
	public static String URLDecoderUTF8(String s) {
		try {
			return URLDecoder.decode(s, "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported UTF-8 Decoding");
		}
	}

	/**
	 * 将普通字符串转换为unicode编码的字符串
	 * 
	 * @param str
	 *            普通字符串
	 * @return unicode编码的字符串
	 */
	public static String convertUnicode(String str) {
		str = (str == null ? "" : str);
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			sb.append("\\u");
			j = (c >>> 8); // 取出高8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);
			j = (c & 0xFF); // 取出低8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1)
				sb.append("0");
			sb.append(tmp);

		}
		return (new String(sb));
	}

	/**
	 * 将unicode编码的字符串转换为普通字符串
	 * 
	 * @param str
	 *            unicode编码的字符串
	 * @return 普通字符串
	 */
	public static String revertUnicode(String str) {
		str = (str == null ? "" : str);
		if (str.indexOf("\\u") == -1)// 如果不是unicode码则原样返回
			return str;
		StringBuffer sb = new StringBuffer(1000);
		for (int i = 0; i < str.length() - 6;) {
			String strTemp = str.substring(i, i + 6);
			String value = strTemp.substring(2);
			int c = 0;
			for (int j = 0; j < value.length(); j++) {
				char tempChar = value.charAt(j);
				int t = 0;
				switch (tempChar) {
				case 'a':
					t = 10;
					break;
				case 'b':
					t = 11;
					break;
				case 'c':
					t = 12;
					break;
				case 'd':
					t = 13;
					break;
				case 'e':
					t = 14;
					break;
				case 'f':
					t = 15;
					break;
				default:
					t = tempChar - 48;
					break;
				}

				c += t * ((int) Math.pow(16, (value.length() - j - 1)));
			}
			sb.append((char) c);
			i = i + 6;
		}
		return sb.toString();
	}

	/**
	 * 利用MD5加密算法进行加密
	 * 
	 * @param s
	 * @return
	 */
	public static String encodeMD5(String s) {
		MD5 md5 = new MD5();
		return md5.getMD5ofStr(s);
	}

	/************************************ 主方法用于测试 *************************************/
	public static void main(String[] args) throws Exception {
		String message = "!";
		String urlEncoder = java.net.URLEncoder.encode(message, "utf-8");
		String encoder = encodeUTF8(message);
		// System.out.println(encode(message, "utf-8"));
		System.out.println(java.net.URLDecoder.decode(urlEncoder, "utf-8"));
		// System.out.println(encodeMD5(message));
		System.out.println(java.net.URLDecoder.decode(encoder, "utf-8"));
		System.out.println(convertUnicode("！"));
		System.out.println(revertUnicode("\uff01"));
		System.out.println(encoder);
		System.out.println(encodeUTF8("！"));
		System.out.println(java.net.URLDecoder.decode("%EF%BC%81", "utf-8"));
	}
}
