package com.web.model.etender;

public class Quoted_Adopt4tbq {
    private Integer billitemid;

    private String adoptnetrate;

    private String adoptnetamount;

    private String adoptremark;

    private String userid;

    public Integer getBillitemid() {
        return billitemid;
    }

    public void setBillitemid(Integer billitemid) {
        this.billitemid = billitemid;
    }

    public String getAdoptnetrate() {
        return adoptnetrate;
    }

    public void setAdoptnetrate(String adoptnetrate) {
        this.adoptnetrate = adoptnetrate == null ? null : adoptnetrate.trim();
    }

    public String getAdoptnetamount() {
        return adoptnetamount;
    }

    public void setAdoptnetamount(String adoptnetamount) {
        this.adoptnetamount = adoptnetamount == null ? null : adoptnetamount.trim();
    }

    public String getAdoptremark() {
        return adoptremark;
    }

    public void setAdoptremark(String adoptremark) {
        this.adoptremark = adoptremark == null ? null : adoptremark.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }
}