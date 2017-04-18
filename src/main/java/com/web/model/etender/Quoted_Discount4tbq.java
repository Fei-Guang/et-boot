package com.web.model.etender;

public class Quoted_Discount4tbq {
    private Integer discountid;

    private String userid;

    private Integer subprojectid;

    private String discount;

    private String discountpercent;

    public Integer getDiscountid() {
        return discountid;
    }

    public void setDiscountid(Integer discountid) {
        this.discountid = discountid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public Integer getSubprojectid() {
        return subprojectid;
    }

    public void setSubprojectid(Integer subprojectid) {
        this.subprojectid = subprojectid;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount == null ? null : discount.trim();
    }

    public String getDiscountpercent() {
        return discountpercent;
    }

    public void setDiscountpercent(String discountpercent) {
        this.discountpercent = discountpercent == null ? null : discountpercent.trim();
    }
}