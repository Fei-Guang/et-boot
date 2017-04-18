package com.web.model.etender;

public class Quoted_Billitem4tbq {
    private Integer billitemid;

    private Integer elementid;

    private Integer tbqbillitemid;

    private Integer tbqpbillitemid;

    private String trade;

    private String unit;

    private Byte type;

    private String netrate;

    private String adjustnetrate;

    private String netamount;

    private String adjustnetamount;

    private Boolean ischange;

    private String qty;

    private Byte pricetype;

    private String remark;

    private String description;
    
    public byte accuracy;
    
    public String vendor="";

    public Integer getBillitemid() {
        return billitemid;
    }

    public void setBillitemid(Integer billitemid) {
        this.billitemid = billitemid;
    }

    public Integer getElementid() {
        return elementid;
    }

    public void setElementid(Integer elementid) {
        this.elementid = elementid;
    }

    public Integer getTbqbillitemid() {
        return tbqbillitemid;
    }

    public void setTbqbillitemid(Integer tbqbillitemid) {
        this.tbqbillitemid = tbqbillitemid;
    }

    public Integer getTbqpbillitemid() {
        return tbqpbillitemid;
    }

    public void setTbqpbillitemid(Integer tbqpbillitemid) {
        this.tbqpbillitemid = tbqpbillitemid;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade == null ? null : trade.trim();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
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

    public Boolean getIschange() {
        return ischange;
    }

    public void setIschange(Boolean ischange) {
        this.ischange = ischange;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty == null ? null : qty.trim();
    }

    public Byte getPricetype() {
        return pricetype;
    }

    public void setPricetype(Byte pricetype) {
        this.pricetype = pricetype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}