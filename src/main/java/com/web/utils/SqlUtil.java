package com.web.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 用于防止sql注入攻击的后台参数处理类
 * 
 * @author liaolh
 * 
 */
public class SqlUtil {

	private static final String H = "#";
	private static final String S = "$";

	/**
	 * mapper.xml中的取值方式为#{}时
	 * 
	 * @param str
	 *            like的查询条件
	 * @param append
	 *            字符串前后部是否拼接“%”
	 * @return
	 */
	public static String likeEscapeH(String str, boolean append) {
		return likeEscapeZ(str, H, append);
	}

	/**
	 * mapper.xml中的取值方式为${}时
	 * 
	 * @param str
	 *            like的查询条件
	 * @param append
	 *            字符串前后部是否拼接“%”
	 * @return
	 */
	public static String likeEscapeS(String str, boolean append) {
		return likeEscapeZ(str, S, append);
	}

	/**
	 * @param str
	 *            like的查询条件
	 * 
	 * @param type
	 *            mapper.xml中的取值方式，只能“#”或“$”
	 * @param append
	 *            字符串前后部是否拼接“%”
	 * @return
	 */
	private static String likeEscapeZ(String str, String type, boolean append) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		StringBuffer buffer = new StringBuffer(); // 拼接顺序不能改变
		if (append) {
			buffer.append("%");
		}
		int len = str.length(); // 注意："]"不能处理
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			switch (c) {
			case '\'':
				if (S.equals(type)) {
					buffer.append("''");// 单引号替换成两个单引号
				} else {
					buffer.append(c);
				}
				break;
			default:
				buffer.append(c);
			}
		}
		if (append) {
			buffer.append("%");
		}
		return buffer.toString();
	}

	/************************************ 主方法用于测试 *************************************/
	public static void main(String[] args) {
		String str = "aaaa]p'100%_a[[][][]][[][]]df[]dfaf]";
		System.out.println("result#: " + likeEscapeH(str, true));
		System.out.println("result$: " + likeEscapeS(str, true));
	}
}
