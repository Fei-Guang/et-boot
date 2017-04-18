package com.web.etools;

import com.web.model.etender.User;

/**
 * 待发邮件通知的对象
 * 
 * @author liaolh
 *
 */
public class NoticeEmail {

	private User user;
	private String subContract;
	private String email;
	private String name;
	private String returnTo;
	private String access_token;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSubContract() {
		return subContract;
	}

	public void setSubContract(String subContract) {
		this.subContract = subContract;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReturnTo() {
		return returnTo;
	}

	public void setReturnTo(String returnTo) {
		this.returnTo = returnTo;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

}
