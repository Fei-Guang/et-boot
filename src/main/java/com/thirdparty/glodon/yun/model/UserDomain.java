package com.thirdparty.glodon.yun.model;

/**
 * 注册到广联云的用户实体
 * 
 * @author zhouxing
 */
public class UserDomain {

	private String identity; // 手机/邮箱/用户名
	private String password; // 密码
	private String name; // 企业名称
	private boolean isEnterprise; // 是否企业账户
	private String captcha; // 手机验证码/注册验证码
	private String returnTo; // 使用邮箱注册后，点击邮箱中的链接地址，激活成功后应返回到的目的URL
	private String locale;// 地区语言，默认是中文zh_CN，英文是en，该语言影响验证邮件的语言格式

	// 调用注册接口之前获取图片验证码时，获取的该验证码对应的key值
	private String captchaKey;

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

	public String getName() {
		if (name == null)
			return "";
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnterprise() {
		return isEnterprise;
	}

	public void setEnterprise(boolean isEnterprise) {
		this.isEnterprise = isEnterprise;
	}

	public String getCaptcha() {
		if (captcha == null)
			return "";
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
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

	public String getCaptchaKey() {
		if (captchaKey == null)
			return "";
		return captchaKey;
	}

	public void setCaptchaKey(String captchaKey) {
		this.captchaKey = captchaKey;
	}

}
