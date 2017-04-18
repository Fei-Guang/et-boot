package com.web.model.etender;

public class Quoted_Element4excel {
    private Integer elementid;

    private Integer subprojectid;

    private String name;

    public Integer getElementid() {
        return elementid;
    }

    public void setElementid(Integer elementid) {
        this.elementid = elementid;
    }

    public Integer getSubprojectid() {
        return subprojectid;
    }

    public void setSubprojectid(Integer subprojectid) {
        this.subprojectid = subprojectid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}