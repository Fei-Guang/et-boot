package com.web.utils;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

@Component
@PropertySource("classpath:init.properties")
public class AppContext implements ServletContextAware {
	
	/*
	 * 发件箱主机地址
	 */
	@Value("${mailServerHost}")
	private String mailServerHost = "mail.glodon.com";

	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}
	
	/*
	 * 发件箱主机端口
	 */
	@Value("${mailServerPort}")
	private int mailServerPort=25;

	public int getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(int mailServerPort) {
		this.mailServerPort = mailServerPort;
	}
	
	/*
	 * 发件箱地址
	 */
	@Value("${mailAddress}")
	private String mailAddress = "global-support@glodon.com";

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	
	/*
	 * 发件箱用户名
	 */
	@Value("${mailUserName}")
	private String mailUserName = "global-support";

	public String getMailUserName() {
		return mailUserName;
	}

	public void setMailUserName(String mailUserName) {
		this.mailUserName = mailUserName;
	}

	/*
	 * 发件箱用户密码
	 */
	@Value("${mailPassword}")
	private String mailPassword = "1qaz@WSX\\#";
	
	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	/**
	 * 容器全局变量
	 */
	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

}
