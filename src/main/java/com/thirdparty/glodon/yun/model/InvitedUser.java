package com.thirdparty.glodon.yun.model;

public class InvitedUser {

	private String identity; // 手机/邮箱/用户名
	private String password; // 初始密码
	private String nickname; // 用户昵称

	public String getIdentity() {
		if (identity == null)
			return "";
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getPassword() {
		if (password == null)
			return "";
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		if (nickname == null)
			return "";
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
