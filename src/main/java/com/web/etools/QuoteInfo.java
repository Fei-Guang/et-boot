package com.web.etools;

import com.utils.DataUtil;

public class QuoteInfo {

	private int billitemid;
	private int billitempid;
	private byte type;
	private String qty;
	private byte pricetype;

	private String netrate;
	private String adjustnetrate;

	private String netamount;
	private String adjustnetamount;

	private String remark;

	private boolean netrateEdit;
	private boolean netamountEdit;
	
	public byte accuracy;
	
	
	public void resetQuote(EvaluationBillItem item, byte accuracy)
	{
							
		this.setNetrate(DataUtil.double2String(item.getNetrate(), accuracy));									
		this.setAdjustnetrate(DataUtil.double2String(item.getAdjustnetrate(), accuracy));								
		this.setNetamount(DataUtil.double2String(item.getNetamount(), accuracy));								
		this.setAdjustnetamount(DataUtil.double2String(item.getAdjustnetamount(), accuracy));
	}

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

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public byte getPricetype() {
		return pricetype;
	}

	public void setPricetype(byte pricetype) {
		this.pricetype = pricetype;
	}

	public String getNetrate() {
		return netrate;
	}

	public void setNetrate(String netrate) {
		this.netrate = netrate;
	}

	public String getAdjustnetrate() {
		return adjustnetrate;
	}

	public void setAdjustnetrate(String adjustnetrate) {
		this.adjustnetrate = adjustnetrate;
	}

	public String getNetamount() {
		return netamount;
	}

	public void setNetamount(String netamount) {
		this.netamount = netamount;
	}

	public String getAdjustnetamount() {
		return adjustnetamount;
	}

	public void setAdjustnetamount(String adjustnetamount) {
		this.adjustnetamount = adjustnetamount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
