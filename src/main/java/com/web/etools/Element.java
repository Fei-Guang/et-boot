package com.web.etools;

import java.util.List;

public class Element {

	private int elementid;
	private int subprojectid;
	private String name;
	private int type;// 1代表章，2代表节
	private List<Element> childElements;

	public int getElementid() {
		return elementid;
	}

	public void setElementid(int elementid) {
		this.elementid = elementid;
	}

	public int getSubprojectid() {
		return subprojectid;
	}

	public void setSubprojectid(int subprojectid) {
		this.subprojectid = subprojectid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Element> getChildElements() {
		return childElements;
	}

	public void setChildElements(List<Element> childElements) {
		this.childElements = childElements;
	}

}
