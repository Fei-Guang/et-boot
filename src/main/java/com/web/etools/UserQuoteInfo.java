package com.web.etools;

import java.util.List;

public class UserQuoteInfo {

	private String userid;
	private String mark;
	private String name;
	private String discountPercent;
	private String discount;
	// 该字段用于保存隐藏部分的总价，这部分由在评标界面不显示的分包工程清单项价格组成
	private String hiddenPrice4SubProject;
	private String fixAmount;

	private List<QuoteInfo> quoteInfo;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(String discountPercent) {
		this.discountPercent = discountPercent;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getHiddenPrice4SubProject() {
		return hiddenPrice4SubProject;
	}

	public void setHiddenPrice4SubProject(String hiddenPrice4SubProject) {
		this.hiddenPrice4SubProject = hiddenPrice4SubProject;
	}
	
	public String getFixAmount() {
		return fixAmount;
	}

	public void setFixAmount(String fixAmount) {
		this.fixAmount = fixAmount;
	}

	public List<QuoteInfo> getQuoteInfo() {
		return quoteInfo;
	}

	public void setQuoteInfo(List<QuoteInfo> quoteInfo) {
		this.quoteInfo = quoteInfo;
	}

}
