package com.utils.email;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleMailSender {

	private static Logger logger = LoggerFactory
			.getLogger(SimpleMailSender.class);

	/**
	 * 发送普通文本邮件
	 * 
	 * @param mailInfo
	 * @return
	 */
	public static boolean sendTextMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MailAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MailAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Set<String> recipients = mailInfo.getToAddresses();
			int num = recipients.size();
			InternetAddress[] addresses = new InternetAddress[num];
			Iterator<String> ite = recipients.iterator();
			int i = 0;
			while (ite.hasNext()) {
				String recipient = ite.next();
				addresses[i] = new InternetAddress(recipient);
				i++;
			}
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipients(Message.RecipientType.TO, addresses);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			logger.error("SendTextMail:", ex);
		}
		return false;
	}

	/**
	 * 发送网页形式邮件，可以携带附件
	 * 
	 * @param mailInfo
	 * @return
	 */
	public static boolean sendHtmlMail(MailSenderInfo mailInfo) {
		// 判断是否需要身份认证
		MailAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MailAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Set<String> recipients = mailInfo.getToAddresses();
			int num = recipients.size();
			InternetAddress[] addresses = new InternetAddress[num];
			Iterator<String> ite = recipients.iterator();
			int i = 0;
			while (ite.hasNext()) {
				String recipient = ite.next();
				addresses[i] = new InternetAddress(recipient);
				i++;
			}
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipients(Message.RecipientType.TO, addresses);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			File[] attachFiles = mailInfo.getAttachFiles();
			if (attachFiles != null) {
				for (File attachFile : attachFiles) {
					BodyPart attachmentBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(attachFile);
					attachmentBodyPart.setDataHandler(new DataHandler(source));
					// MimeUtility.encodeWord可以避免文件名乱码
					attachmentBodyPart.setFileName(MimeUtility
							.encodeWord(attachFile.getName()));
					mainPart.addBodyPart(attachmentBodyPart);
				}
			}
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			logger.error("SendHtmlMail:", ex);
		} catch (UnsupportedEncodingException ex) {
			logger.error("SendHtmlMail:", ex);
		}
		return false;
	}

	/***************************************** 主方法用于测试 **************************************************/
	public static void main(String[] args) {
		// 设置邮件服务器信息
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.126.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		// 邮箱用户名
		mailInfo.setUserName("liaolinghao");
		// 邮箱密码
		mailInfo.setPassword("xxxx");
		// 发件人邮箱
		mailInfo.setFromAddress("liaolinghao@126.com");
		// 收件人邮箱
		Set<String> toAddresses = new HashSet<String>();
		toAddresses.add("26089183@qq.com");
		mailInfo.setToAddresses(toAddresses);
		// 邮件标题
		mailInfo.setSubject("测试Java程序发送邮件");
		// 邮件内容
		StringBuffer buffer = new StringBuffer();
		buffer.append("JavaMail 1.4.5 jar包下载地址：http://www.oracle.com/technetwork/java/index-138643.html\n");
		buffer.append("JAF 1.1.1 jar包下载地址：http://www.oracle.com/technetwork/java/javase/downloads/index-135046.html");
		mailInfo.setContent(buffer.toString());

		// File[] attachFiles = new File[2];
		// attachFiles[0] = new File("G:/饮食健康.docx");
		// attachFiles[1] = new File("G:/workInfo.ini");
		// mailInfo.setAttachFiles(attachFiles);

		// 发送邮件
		// 发送文体格式
		// SimpleMailSender.sendTextMail(mailInfo);
		// 发送html格式
		SimpleMailSender.sendHtmlMail(mailInfo);
		System.out.println("邮件发送完毕");
	}

}
