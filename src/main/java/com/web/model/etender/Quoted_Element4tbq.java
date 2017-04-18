package com.web.model.etender;

public class Quoted_Element4tbq {
    private Integer elementid;

    private Integer subprojectid;

    private Integer tbqelementid;

    private Integer tbqpelementid;

    private String name;

    private Byte type;

    private Boolean checkout;

    private String checkoutuserid;

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

    public Integer getTbqelementid() {
        return tbqelementid;
    }

    public void setTbqelementid(Integer tbqelementid) {
        this.tbqelementid = tbqelementid;
    }

    public Integer getTbqpelementid() {
        return tbqpelementid;
    }

    public void setTbqpelementid(Integer tbqpelementid) {
        this.tbqpelementid = tbqpelementid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Boolean getCheckout() {
        return checkout;
    }

    public void setCheckout(Boolean checkout) {
        this.checkout = checkout;
    }

    public String getCheckoutuserid() {
        return checkoutuserid;
    }

    public void setCheckoutuserid(String checkoutuserid) {
        this.checkoutuserid = checkoutuserid == null ? null : checkoutuserid.trim();
    }
}