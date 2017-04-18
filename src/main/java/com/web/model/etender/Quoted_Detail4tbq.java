package com.web.model.etender;

public class Quoted_Detail4tbq {
    private Integer detailid;

    private Integer billitemid;

    private String userid;

    private String netrate;

    private String adjustnetrate;

    private String netamount;

    private String adjustnetamount;

    private String remark;

    public Integer getDetailid() {
        return detailid;
    }

    public void setDetailid(Integer detailid) {
        this.detailid = detailid;
    }

    public Integer getBillitemid() {
        return billitemid;
    }

    public void setBillitemid(Integer billitemid) {
        this.billitemid = billitemid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getNetrate() {
        return netrate;
    }

    public void setNetrate(String netrate) {
        this.netrate = netrate == null ? null : netrate.trim();
    }

    public String getAdjustnetrate() {
        return adjustnetrate;
    }

    public void setAdjustnetrate(String adjustnetrate) {
        this.adjustnetrate = adjustnetrate == null ? null : adjustnetrate.trim();
    }

    public String getNetamount() {
        return netamount;
    }

    public void setNetamount(String netamount) {
        this.netamount = netamount == null ? null : netamount.trim();
    }

    public String getAdjustnetamount() {
        return adjustnetamount;
    }

    public void setAdjustnetamount(String adjustnetamount) {
        this.adjustnetamount = adjustnetamount == null ? null : adjustnetamount.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}