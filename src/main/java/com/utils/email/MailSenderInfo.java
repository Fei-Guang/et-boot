package com.utils.email;

import java.io.File;
import java.util.Properties;
import java.util.Set;

/**
 * 记录发送邮件所需的各种信息，如发送邮件服务器的地址、端口号、发件人邮箱、收件人邮箱等等
 * 
 * @author liao.lh
 * 
 */
public class MailSenderInfo {

	// 发送邮件的服务器的IP(或主机地址)
	private String mailServerHost;

	// 发送邮件的服务器的端口
	private String mailServerPort = "25";

	// 发件人邮箱地址
	private String fromAddress;

	// 收件人邮箱地址，可能包含多个收件人
	private Set<String> toAddresses;

	// 登录邮件发送服务器的用户名
	private String userName;

	// 登录邮件发送服务器的密码
	private String password;

	// 是否需要身份验证
	private boolean validate = false;

	// 邮件主题
	private String subject;

	// 邮件的文本内容
	private String content;

	// 邮件附件的文件名
	private File[] attachFiles;

	public Properties getProperties() {
		Properties p = new Properties();
		p.put("mail.smtp.host", this.mailServerHost);
		p.put("mail.smtp.port", this.mailServerPort);
		p.put("mail.smtp.auth", validate ? "true" : "false");
		return p;
	}

	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public File[] getAttachFiles() {
		return attachFiles;
	}

	public void setAttachFiles(File[] attachFiles) {
		this.attachFiles = attachFiles;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * 部分邮箱要求登录密码和第三方客户端连接密码分离，此处注意密码的设置
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getToAddresses() {
		return toAddresses;
	}

	public void setToAddresses(Set<String> toAddresses) {
		this.toAddresses = toAddresses;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String textContent) {
		this.content = textContent;
	}

}
