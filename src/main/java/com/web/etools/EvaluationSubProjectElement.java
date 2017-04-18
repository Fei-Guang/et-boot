package com.web.etools;

import java.util.List;

/**
 * 评标分包工程类目
 * 
 * @author liaolh
 *
 */
public class EvaluationSubProjectElement {

	private int subprojectid;
	private int queryprojectid;
	private String name;
	private List<Element> elements;

	public int getSubprojectid() {
		return subprojectid;
	}

	public void setSubprojectid(int subprojectid) {
		this.subprojectid = subprojectid;
	}

	public int getQueryprojectid() {
		return queryprojectid;
	}

	public void setQueryprojectid(int queryprojectid) {
		this.queryprojectid = queryprojectid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

}
