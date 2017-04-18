package com.web.model.etender;

import java.util.Date;

public class Supplier_Subproject {
    private Integer suppliersubprojectid;

    private Integer subprojectid;

    private String name;

    private String email;

    private String telephone;

    private String trade;

    private String level;

    private String address;

    private String contacts;

    private String userid;

    private String attachinfo;

    private Date createtime;

    private Date receivedtime;

    private Date firstsavetime;

    private Date submittime;

    private Date reviewtime;

    private Long logiccounter;

    private Boolean logicdelete;

    public Integer getSuppliersubprojectid() {
        return suppliersubprojectid;
    }

    public void setSuppliersubprojectid(Integer suppliersubprojectid) {
        this.suppliersubprojectid = suppliersubprojectid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade == null ? null : trade.trim();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts == null ? null : contacts.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getAttachinfo() {
        return attachinfo;
    }

    public void setAttachinfo(String attachinfo) {
        this.attachinfo = attachinfo == null ? null : attachinfo.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getReceivedtime() {
        return receivedtime;
    }

    public void setReceivedtime(Date receivedtime) {
        this.receivedtime = receivedtime;
    }

    public Date getFirstsavetime() {
        return firstsavetime;
    }

    public void setFirstsavetime(Date firstsavetime) {
        this.firstsavetime = firstsavetime;
    }

    public Date getSubmittime() {
        return submittime;
    }

    public void setSubmittime(Date submittime) {
        this.submittime = submittime;
    }

    public Date getReviewtime() {
        return reviewtime;
    }

    public void setReviewtime(Date reviewtime) {
        this.reviewtime = reviewtime;
    }

    public Long getLogiccounter() {
        return logiccounter;
    }

    public void setLogiccounter(Long logiccounter) {
        this.logiccounter = logiccounter;
    }

    public Boolean getLogicdelete() {
        return logicdelete;
    }

    public void setLogicdelete(Boolean logicdelete) {
        this.logicdelete = logicdelete;
    }
}