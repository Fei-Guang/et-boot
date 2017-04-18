package com.thirdparty.glodon.yun.model;

import java.util.ArrayList;
import java.util.List;

public class InviteUserDomain {

	private String message;
	private String returnTo; // 使用邮箱注册后，点击邮箱中的链接地址，激活成功后应返回到的目的URL
	private String locale;// 地区语言

	private List<InvitedUser> invitedUserList;// 用户信息列表

	public String getMessage() {
		if (message == null)
			return "";
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReturnTo() {
		if (returnTo == null)
			return "";
		return returnTo;
	}

	public void setReturnTo(String returnTo) {
		this.returnTo = returnTo;
	}

	public String getLocale() {
		if (locale == null)
			return "en";
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public List<InvitedUser> getInvitedUserList() {
		if (invitedUserList == null)
			return new ArrayList<InvitedUser>();
		return invitedUserList;
	}

	public void setInvitedUserList(List<InvitedUser> invitedUserList) {
		this.invitedUserList = invitedUserList;
	}

}
