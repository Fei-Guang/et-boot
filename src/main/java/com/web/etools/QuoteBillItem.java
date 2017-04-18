package com.web.etools;

import com.utils.DataUtil;
import com.utils.StringUtil;

public class QuoteBillItem {

	private int billitemid;
	private int billitempid;
	private String description;
	private String trade;
	private String unit;
	private byte type;
	private String qty;
	private String netrate;
	private String netamount;
	private byte pricetype;
	private String remark;
	private boolean qtyShow;
	private boolean netrateShow;
	private boolean netamountShow;
	private boolean netrateEdit;
	private boolean netamountEdit;

	public int getBillitemid() {
		return billitemid;
	}

	public void setBillitemid(int billitemid) {
		this.billitemid = billitemid;
	}

	public int getBillitempid() {
		return billitempid;
	}

	public void setBillitempid(int billitempid) {
		this.billitempid = billitempid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getQty() {
		if (!StringUtil.isNull(qty) && DataUtil.isNumber(qty)) {
			return qty;
		} else {
			return "";
		}
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getNetrate() {
		if (!StringUtil.isNull(netrate) && DataUtil.isNumber(netrate)) {
			return netrate;
		} else {
			return "";
		}
	}

	public void setNetrate(String netrate) {
		this.netrate = netrate;
	}

	public String getNetamount() {
		if (!StringUtil.isNull(netamount) && DataUtil.isNumber(netamount)) {
			return netamount;
		} else {
			return "";
		}
	}

	public void setNetamount(String netamount) {
		this.netamount = netamount;
	}

	public byte getPricetype() {
		return pricetype;
	}

	public void setPricetype(byte pricetype) {
		this.pricetype = pricetype;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isQtyShow() {
		return qtyShow;
	}

	public void setQtyShow(boolean qtyShow) {
		this.qtyShow = qtyShow;
	}

	public boolean isNetrateShow() {
		return netrateShow;
	}

	public void setNetrateShow(boolean netrateShow) {
		this.netrateShow = netrateShow;
	}

	public boolean isNetamountShow() {
		return netamountShow;
	}

	public void setNetamountShow(boolean netamountShow) {
		this.netamountShow = netamountShow;
	}

	public boolean isNetrateEdit() {
		return netrateEdit;
	}

	public void setNetrateEdit(boolean netrateEdit) {
		this.netrateEdit = netrateEdit;
	}

	public boolean isNetamountEdit() {
		return netamountEdit;
	}

	public void setNetamountEdit(boolean netamountEdit) {
		this.netamountEdit = netamountEdit;
	}

}
