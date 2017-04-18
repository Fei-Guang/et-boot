package com.utils.office.excel;

public class WebColor {

	private String hexCode; // 颜色的唯一标识

	public WebColor(String htmlColor) {
		hexCode = htmlColor;
	}

	public String getHexCode() {
		return hexCode;
	}

	public void setHexCode(String hexCode) {
		this.hexCode = hexCode;
	}

	public String getIdentifier() {
		return hexCode;
	}

}
