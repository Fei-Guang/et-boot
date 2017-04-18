package com.web.taglib;

import com.utils.StringUtil;

public class StringTag {

	/**
	 * 获取一个字符串的长度
	 * 
	 * @param str
	 * @return int
	 */
	public static int length(String str) {
		return StringUtil.getLength(str);
	}

	/**
	 * 截取字符串前面指定位数的子串
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String substring(String str, int length) {
		return StringUtil.subString(str, length);
	}

	/**
	 * 隐藏隐私信息
	 * 
	 * @param str
	 * @return
	 */
	public static String hidePrivacy(String str) {
		if (StringUtil.isTelePhone(str)) {
			return str.substring(0, str.length() - 5) + "***"
					+ str.substring(str.length() - 2);
		}
		if (StringUtil.isMobilePhone(str)) {
			return str.substring(0, 3) + "******"
					+ str.substring(str.length() - 2);
		}
		if (StringUtil.isEmail(str)) {
			int index = str.indexOf("@");
			String head = str.substring(0, index);
			return head.charAt(0) + "***" + head.charAt(head.length() - 1)
					+ str.substring(index);
		}
		return str;
	}

}
