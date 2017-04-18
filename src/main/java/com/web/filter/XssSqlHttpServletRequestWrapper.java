package com.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.utils.Coder;
import com.utils.HtmlUtil;
import com.web.common.ConstantData;

public class XssSqlHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final static Logger logger = LoggerFactory
			.getLogger(XssSqlHttpServletRequestWrapper.class);

	public XssSqlHttpServletRequestWrapper(HttpServletRequest servletRequest) {
		super(servletRequest);
	}

	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = cleanXSS(values[i]);
		}
		return encodedValues;
	}

	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}
		return cleanXSS(value);
	}

	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value == null)
			return null;
		return cleanXSS(value);
	}

	private String cleanXSS(String value) {
		// TODO 可以自定义处理，也可以利用开源的XSS HTMLFilter来处理或者利用Jsoup来处理
		// value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		// 第一步，解码，这里统一进行UTF-8解码，在各个控制器中不再需要进行单独解码，同时为了防止+号被转换为' '丢失，这里特别处理下
		String[] strs = value.split("\\+");
		StringBuffer unsafe = new StringBuffer();
		for (String str : strs) {
			unsafe.append(Coder.URLDecoderUTF8(str));
			unsafe.append("+");
		}
		String decoder = unsafe.substring(0, unsafe.length() - 1);
		// 第二步，字符反转义，这里先进行一次反转，防止一些编辑器先转码，这样后续进行xss处理时，被忽略了
		String unescape = StringEscapeUtils.unescapeHtml4(decoder);
		// 第三步，xss过滤，这里对参数进行xss处理，防止xss漏洞
		String filter = HtmlUtil
				.xssFilterByJsoup(unescape, ConstantData.DOMAIN);
		// 第四步，字符反转义，这里再进行一次反转，防止xss过滤时，一些字符被转义，导致参数解析错误
		String info = StringEscapeUtils.unescapeHtml4(filter);
		logger.debug("Xss clean:" + info);
		return info;
	}
}
