package com.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class HtmlUtil {

	private static Whitelist xss_filter = Whitelist.relaxed();

	static {
		xss_filter.addTags("embed", "object", "param", "span", "div");
		xss_filter.addAttributes(":all", "style", "class", "id", "name");
		xss_filter.addAttributes("object", "width", "height", "classid",
				"codebase");
		xss_filter.addAttributes("param", "name", "value");
		xss_filter.addAttributes("embed", "src", "quality", "width", "height",
				"allowFullScreen", "allowScriptAccess", "flashvars", "name",
				"type", "pluginspage");
		// 保留相对链接设置只有在提供链接基地址的情况下过滤才有效
		xss_filter = xss_filter.preserveRelativeLinks(true);
	}

	/**
	 * 给html字符串进行xss过滤处理，利用JSOUP实现定制，自由度更高
	 * 
	 * @param html
	 * @param baseUri
	 *            该基地址对于是否保留相对链接十分重要
	 * @return
	 */
	public static String xssFilterByJsoup(String html, String baseUri) {
		if (StringUtils.isBlank(html))
			return "";
		if (StringUtils.isBlank(baseUri)) {
			return Jsoup.clean(html, xss_filter);
		} else {
			return Jsoup.clean(html, baseUri, xss_filter);
		}
	}

}
