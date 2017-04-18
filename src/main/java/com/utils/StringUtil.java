package com.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * 海盗旗符号
	 */
	public static String Black_Flag = "\u0008";

	/**
	 * 卵子符号
	 */
	public static String OVUM = "\u000c";

	/**
	 * 星形符号
	 */
	public static String STELLATE = "\u203B";

	/**
	 * 去除左边的空格
	 * 
	 * @param str
	 * @return
	 */
	public static String trimLeft(String str) {
		if (str == null)
			return null;

		char[] ca = str.toCharArray();
		int i = 0, len = ca.length;
		while (i < len && ca[i] <= ' ')
			i++;
		return str.substring(i);
	}

	/**
	 * 去除右边的空格
	 * 
	 * @param str
	 * @return
	 */
	public static String trimRight(String str) {
		if (str == null)
			return null;

		char[] ca = str.toCharArray();
		int i = ca.length;
		while (i > 0 && ca[i - 1] <= ' ')
			i--;
		return str.substring(0, i);
	}

	/**
	 * 截取指定字符串右边的字符串
	 * 
	 * @param str
	 * @param delimiter
	 * @return
	 */
	public static String trimLeft(String str, String delimiter) {
		if (str == null)
			return null;
		int i = str.indexOf(delimiter);
		if (i == -1)
			return str;
		return str.substring(i + 1);
	}

	/**
	 * 截取指定字符串左边的字符串
	 * 
	 * @param str
	 * @param delimiter
	 * @return
	 */
	public static String trimRight(String str, String delimiter) {
		if (str == null)
			return null;
		int i = str.lastIndexOf(delimiter);
		if (i == -1)
			return str;
		return str.substring(0, i);
	}

	/**
	 * 删除字符串中的指定字符
	 * 
	 * @param str
	 * @param c
	 * @return
	 */
	public static String deleteChar(String str, char c) {
		char[] chars = str.toCharArray();
		StringBuffer buffer = new StringBuffer();
		for (char ch : chars) {
			if (ch != c) {
				buffer.append(ch);
			}
		}
		return buffer.toString().trim();
	}

	/**
	 * 判断第一个字符串是否以第二个字符串开头，不区分大小写
	 * 
	 * @param str1
	 *            用于判断的字符串
	 * @param str2
	 *            标志串
	 * @return
	 */
	public static boolean startsWithIgnoreCase(String str1, String str2) {
		return str1.toLowerCase().startsWith(str2.toLowerCase());
	}

	/**
	 * 判断第一个字符串是否以第二个字符串结尾，不区分大小写
	 * 
	 * @param str1
	 *            用于判断的字符串
	 * @param str2
	 *            标志串
	 * @return
	 */
	public static boolean endsWithIgnoreCase(String str1, String str2) {
		return str1.toLowerCase().endsWith(str2.toLowerCase());
	}

	/**
	 * 判断字符串是否为空串或者不为空但无任何意义
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str) {
		return str == null || str.trim().equalsIgnoreCase("".trim());
	}

	/**
	 * 处理字符串，将null转换为""
	 * 
	 * @param str
	 * @return
	 */
	public static String processNullStr(String str) {
		if (isNull(str)) {
			return "";
		} else {
			return str;
		}
	}

	/**
	 * 以iso-8859-1对字符串进行编码
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeStrByISO_8859_1(String str)
			throws UnsupportedEncodingException {
		return new String(str.getBytes("utf-8"), "iso-8859-1");
	}

	/**
	 * 以utf-8对字符串进行解码，该方法一般用于在网页传值时，解析tomcat编码引起的中文问题
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeStrByUTF_8(String str)
			throws UnsupportedEncodingException {
		return new String(str.getBytes("iso-8859-1"), "utf-8");
	}

	/**
	 * 用于对URL中传递的指定特殊字符转义
	 * 
	 * @param str
	 * @param chars
	 * @return
	 */
	public static String escape(String str, char[] chars) {
		if (ArrayUtil.contain(chars, '%')) {
			str = str.replace("%", "%25");
		}
		if (ArrayUtil.contain(chars, '+')) {
			str = str.replace("+", "%2B");
		}
		if (ArrayUtil.contain(chars, '/')) {
			str = str.replace("/", "%2F");
		}
		if (ArrayUtil.contain(chars, '\\')) {
			str = str.replace("\\", "%5C");
		}
		if (ArrayUtil.contain(chars, '?')) {
			str = str.replace("?", "%3F");
		}
		if (ArrayUtil.contain(chars, '#')) {
			str = str.replace("#", "%23");
		}
		if (ArrayUtil.contain(chars, '&')) {
			str = str.replace("&", "%26");
		}
		if (ArrayUtil.contain(chars, '=')) {
			str = str.replace("=", "%3D");
		}
		if (ArrayUtil.contain(chars, '.')) {
			str = str.replace(".", "%2E");
		}
		if (ArrayUtil.contain(chars, ':')) {
			str = str.replace(":", "%3A");
		}
		if (ArrayUtil.contain(chars, '\"')) {
			str = str.replace("\"", "%22");
		}
		if (ArrayUtil.contain(chars, '(')) {
			str = str.replace("(", "%28");
		}
		if (ArrayUtil.contain(chars, ')')) {
			str = str.replace(")", "%29");
		}
		if (ArrayUtil.contain(chars, ',')) {
			str = str.replace(",", "%2C");
		}
		if (ArrayUtil.contain(chars, ';')) {
			str = str.replace(";", "%3B");
		}
		if (ArrayUtil.contain(chars, '<')) {
			str = str.replace("<", "%3C");
		}
		if (ArrayUtil.contain(chars, '>')) {
			str = str.replace(">", "%3E");
		}
		if (ArrayUtil.contain(chars, '@')) {
			str = str.replace("@", "%40");
		}
		if (ArrayUtil.contain(chars, '|')) {
			str = str.replace("|", "%7C");
		}
		if (ArrayUtil.contain(chars, ' ')) {
			str = str.replace(" ", "%20");
		}
		return str;
	}

	/**
	 * 只提取字符串中的数字
	 * 
	 * @param str
	 * @return
	 */
	public static String getNumber(String str) {
		return str.replaceAll("\\D", "");
	}

	/**
	 * 获取字符串中所有单词字符：[a-zA-Z_0-9]
	 * 
	 * @param str
	 * @return
	 */
	public static String getWordCharacter(String str) {
		return str.replaceAll("\\W", "");
	}

	/**
	 * 反转字符串的顺序
	 * 
	 * @param str
	 * @return
	 */
	public static String reverse(String str) {
		StringBuffer sb = new StringBuffer(str);
		return sb.reverse().toString().trim();
	}

	/**
	 * 判断字符串是否包含非法字符数组中的字符
	 * 
	 * @param str
	 *            要验证的字符串
	 * @param cs
	 *            非法字符数组
	 * @return
	 */
	public static boolean isContainIllegalChars(String str, char[] cs) {
		for (char illegalChar : cs) {
			if (ArrayUtil.contain(str.toCharArray(), illegalChar)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取字符串的真实长度，对于一个中文字符按两个英文字符长度计算
	 * 
	 * @param str
	 * @return
	 */
	public static int getLength(String str) {
		int len = 0;
		for (int i = 0; i < str.length(); i++) {
			int ch = str.charAt(i);
			if (ch > 255)
				len += 2;
			else
				len++;
		}
		return len;
	}

	/**
	 * 截取字符串前面指定长度的子串，对于一个中文字符按两个英文字符长度计算
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String subString(String str, int length) {
		StringBuffer sb = new StringBuffer();
		int len = 0;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch > 255)
				len += 2;
			else
				len++;
			if (len < length) {
				sb.append(ch);
			} else {
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * 判断字符串是否是IP地址
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isIp(String ip) {
		boolean b = false;
		ip = ip.trim();
		if (ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
			String s[] = ip.split("\\.");
			if (Integer.parseInt(s[0]) < 255)
				if (Integer.parseInt(s[1]) < 255)
					if (Integer.parseInt(s[2]) < 255)
						if (Integer.parseInt(s[3]) < 255)
							b = true;
		}
		return b;
	}

	/**
	 * 是否是手机号，国家号码段分配如下：
	 * 
	 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、182、183、184、187、188
	 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、181、189、（1349卫通）
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobilePhone(String mobiles) {
		Pattern p = Pattern.compile("((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 是否是座机号码
	 * 
	 * @param telePhone
	 * @return
	 */
	public static boolean isTelePhone(String telePhone) {
		Pattern p = Pattern.compile("((0\\d{2,3})-?)?(\\d{7,8})(-(\\d{3,}))?");
		Matcher m = p.matcher(telePhone);
		return m.matches();
	}

	/**
	 * 是否是超链接
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isHyperlink(String str) {
		Pattern p = Pattern.compile("(https|http)://\\S+");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 是否是邮箱
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		Pattern p = Pattern
				.compile("(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z-]+))@([a-zA-Z0-9-]+[.])+(net|NET|asia|ASIA|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT|cn|CN|cc|CC|sg|SG|([a-zA-Z]*))");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 提取电话号码或者手机号码
	 * 
	 * @param text
	 * @return
	 */
	public static String pickUpPhone(String text) {
		Pattern p = Pattern
				.compile("(?<!\\d)(?:(?:((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8})|(?:((0\\d{2,3})-?)?(\\d{7,8})(-(\\d{3,}))?))(?!\\d)");
		Matcher matcher = p.matcher(text);
		List<String> phones = new ArrayList<String>();
		while (matcher.find()) {
			String phone = matcher.group();
			if (!phones.contains(phone)) {
				phones.add(phone);
			}
		}
		return getCollectionStringDescription(phones, ",");
	}

	/**
	 * 提取电子邮件地址
	 * 
	 * @param text
	 * @return
	 */
	public static String pickUpEmail(String text) {
		Pattern p = Pattern
				.compile("(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z-]+))@([a-zA-Z0-9-]+[.])+(net|NET|asia|ASIA|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT|cn|CN|cc|CC|sg|SG|([a-zA-Z]*))");
		Matcher matcher = p.matcher(text);
		List<String> emails = new ArrayList<String>();
		while (matcher.find()) {
			String email = matcher.group();
			if (!emails.contains(email)) {
				emails.add(email);
			}
		}
		return getCollectionStringDescription(emails, ",");
	}

	/**
	 * 提取超链接，该方法对于超链接的结束依赖于文本中的空白字符
	 * 
	 * @param text
	 * @return
	 */
	public static String pickUpHyperlink(String text) {
		// ((https://)|(http://)|(ftp://)|(rtsp://)|(mms://))? 代表各种链接的开头
		// (\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))+ 代表链接中的域名
		// ((:\\d+)?) 代表端口号
		// (/(\\S)+)* 代表网站链接具体页面标识
		Pattern p = Pattern
				.compile("((https://)|(http://)|(ftp://)|(rtsp://)|(mms://))?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))+((:\\d+)?)(/(\\S)+)*");
		Pattern p2 = Pattern.compile("(\\d+(\\.)?)+");// 用于排除无效的匹配信息
		Matcher matcher = p.matcher(text);
		List<String> hyperlinks = new ArrayList<String>();
		while (matcher.find()) {
			String hyperlink = matcher.group();
			if (p2.matcher(hyperlink).matches()) {
				continue;
			}
			if (!hyperlinks.contains(hyperlink)) {
				hyperlinks.add(hyperlink);
			}
		}
		return getCollectionStringDescription(hyperlinks);
	}

	/**
	 * 判断两个字符串是不是相同，忽略大小写与两边的空白字符，并且null与""串相当
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isStrEqualsIgnoreCase(String str1, String str2) {
		if (str1 == null) {
			str1 = "";
		}
		if (str2 == null) {
			str2 = "";
		}
		return str1.trim().equalsIgnoreCase(str2.trim());
	}

	/**
	 * 判断两个字符串是不是相同，忽略两边的空白字符，并且null与""串相当
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isStrEquals(String str1, String str2) {
		if (str1 == null) {
			str1 = "";
		}
		if (str2 == null) {
			str2 = "";
		}
		return str1.trim().equals(str2.trim());
	}

	/**
	 * 清除字符串中的html标记
	 * 
	 * @param str
	 * @return
	 */
	public static String clearHtmlMark(String str) {
		if (str == null) {
			return null;
		}
		return str.replaceAll("<.*?>|(&nbsp;)", "").trim();
	}

	/**
	 * 获取唯一性编码
	 * 
	 * @return
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18)
				+ s.substring(19, 23) + s.substring(24);
	}

	/**
	 * 获取指定长度的唯一性编码，该编码最长支持32位，并且不保证编码唯一的正确性，尤其在编码位数越小的时候，重复的可能性越大
	 * 
	 * @param length
	 * @return
	 */
	public static String getUniqueId(int length) {
		String uuid = getUUID();
		if (length > uuid.length()) {
			return uuid;
		} else {
			return uuid.substring(0, length);
		}
	}

	/**
	 * 获取行分隔符
	 * 
	 * @return
	 */
	public static String getLineSeparator() {
		return System.getProperty("line.separator");
	}

	/**
	 * 获取文件路劲分隔符
	 * 
	 * @return
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	/**
	 * 获取集合的字符串描述串,该描述串由集合的所有元素拼接构成
	 * 
	 * @param c
	 * @return
	 */
	public static String getCollectionStringDescription(Collection<?> c) {
		return getCollectionStringDescription(c, Black_Flag);
	}

	/**
	 * 获取集合的字符串描述串,该描述串由集合的所有元素拼接构成
	 * 
	 * @param c
	 * @param delimiter
	 *            信息分割符
	 * @return
	 */
	public static String getCollectionStringDescription(Collection<?> c,
			String delimiter) {
		if (c == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		Iterator<?> iterator = c.iterator();
		while (iterator.hasNext()) {
			String str = iterator.next().toString();
			if (!StringUtil.isNull(str)) {
				sb.append(str).append(delimiter);
			}
		}
		int len = sb.length();
		if (len > 0) {
			sb.deleteCharAt(len - 1);
		}
		return sb.toString().trim();
	}

	/**
	 * 根据描述串构造包含每个元素字符串表达式的集合
	 * 
	 * @param description
	 * @return
	 */
	public static List<String> getListByDescription(String description) {
		return getListByDescription(description, Black_Flag);
	}

	/**
	 * 根据描述串构造包含每个元素字符串表达式的集合
	 * 
	 * @param description
	 * @param delimiter
	 *            信息分割符
	 * @return
	 */
	public static List<String> getListByDescription(String description,
			String delimiter) {
		if (description == null) {
			return null;
		}
		String[] strs = description.split(delimiter);
		List<String> list = new ArrayList<String>();
		for (String str : strs) {
			if (!StringUtil.isNull(str)) {
				list.add(str);
			}
		}
		return list;
	}

	/**
	 * 插入字符串到指定字符串的指定位置，返回新构造的字符串
	 * 
	 * @param str
	 *            旧字符串
	 * @param insertStr
	 *            待插入字符串
	 * @param index
	 *            指定位置
	 * @return
	 */
	public static String insert(String str, String insertStr, int index) {
		if (str == null) {
			return insertStr;
		}
		if (insertStr == null) {
			return str;
		}
		int length = str.length();
		if (index <= 0) {
			return insertStr + str;
		} else if (index >= length) {
			return str + insertStr;
		} else {
			String str1 = str.substring(0, index);
			String str2 = str.substring(index);
			return str1 + insertStr + str2;
		}
	}

	/**
	 * 将字节转换为十六进制字符串
	 * 
	 * @param mByte
	 * @return
	 */
	public static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];
		String s = new String(tempArr);
		return s;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param byteArray
	 * @return
	 */
	public static String byteToHexStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/************************************ 主方法用于测试 *************************************/
	public static void main(String[] args) {
		// String test = "  test   ";
		// System.out.println("处理前长度：" + test.length());
		// test = trimLeft(test);
		// test = trimRight(test);
		// System.out.println("处理后长度：" + test.length());
		// String test2 = "123;456;789";
		// test2 = trimLeft(test2, ";");
		// test2 = trimRight(test2, ";");
		// System.out.println("处理字符串：" + test2);
		// System.out.println(deleteChar("123.456.4der/!`#", '.'));
		// System.out.println(isNull(null));
		// System.out.println(escape("%3E.12+ab", new char[] { '%', '.' }));
		// System.out.println(reverse("502"));
		// char[] cs = new char[] { '`', '~', '!', '@', '#', '$', '%', '^', '&',
		// '*', '|', '\\', '{', '}', '[', ']', ':', ';', '\"', '\'', '<',
		// '>', '?', ',', '.', '/' };
		// System.out.println(isContainIllegalChars("liao", cs));
		// System.out.println(getLength("123我们"));
		// String s = " 111.110.133.244 ";
		// System.out.println(isIp(s));
		// System.out.println(endsWithIgnoreCase("123.4AS", ".4ss"));
		// System.out.println(pickUpEmail("123,.liaolinghao@126.com1234"));
		System.out.println(isEmail("mult@mult.co.xy"));
	}

}
