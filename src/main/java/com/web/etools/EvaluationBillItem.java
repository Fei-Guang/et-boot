package com.web.etools;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


import com.utils.DataUtil;
import com.utils.StringUtil;
import com.web.controller.CommonController;

public class EvaluationBillItem {
	
	static Logger logger = LoggerFactory
			.getLogger(EvaluationBillItem.class);

	private int billitemid;
	private int billitempid;
	private String description;
	private String trade;
	private String elementName;
	private String unit;
	private byte type;
	private String qty;
	private byte pricetype;

	private String adjustnetrate;// 标底单价
	private String adjustnetamount;// 标底总价
	private String netrate;// 标底原始单价
	private String netamount;// 标底原始总价
	private String remark;// 标底备注

	private String adoptedNetrate;// 采纳单价
	private String adoptedNetamount;// 采纳总价
	private String adoptedRemark;// 采纳备注
	private String adoptedUserID;// 采纳报价人标识
	private String adoptedUserMark;// 采纳报价人标志

	private boolean qtyShow;
	private boolean netrateShow;
	private boolean netamountShow;
	private boolean netrateEdit;
	private boolean netamountEdit;
	public byte accuracy;

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

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
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

	public byte getPricetype() {
		return pricetype;
	}

	public void setPricetype(byte pricetype) {
		this.pricetype = pricetype;
	}

	public String getAdjustnetrate() {
		if (!StringUtil.isNull(adjustnetrate)
				&& DataUtil.isNumber(adjustnetrate)) {
			return adjustnetrate;
		} else {
			return "";
		}
	}

	public void setAdjustnetrate(String adjustnetrate) {
		this.adjustnetrate = adjustnetrate;
	}

	public String getAdjustnetamount() {
		if (!StringUtil.isNull(adjustnetamount)
				&& DataUtil.isNumber(adjustnetamount)) {
			return adjustnetamount;
		} else {
			return "";
		}
	}

	public void setAdjustnetamount(String adjustnetamount) {
		this.adjustnetamount = adjustnetamount;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAdoptedNetrate() {
		if (!StringUtil.isNull(adoptedNetrate) ){
				if ( !DataUtil.isNumber(adoptedNetrate) ){ 
					logger.info("adoptedNetrate={} not a number", adoptedNetrate);
					adoptedNetrate = adoptedNetrate.replaceAll(",", "");
				}
				return adoptedNetrate;		
		} else {
			return "";
		}
	}

	public void setAdoptedNetrate(String adoptedNetrate) {
		this.adoptedNetrate = adoptedNetrate;
	}

	public String getAdoptedNetamount() {
		if (!StringUtil.isNull(adoptedNetamount)){
			 if ( ! DataUtil.isNumber(adoptedNetamount) ) 
			 {
				 logger.info("adoptedNetamount={} not a number", adoptedNetamount);
				 adoptedNetamount = adoptedNetamount.replaceAll(",", "");
			 }
			 return adoptedNetamount;
		} 
		return "";
		
	}

	public void setAdoptedNetamount(String adoptedNetamount) {
		this.adoptedNetamount = adoptedNetamount;
	}

	public String getAdoptedRemark() {
		return adoptedRemark;
	}

	public void setAdoptedRemark(String adoptedRemark) {
		this.adoptedRemark = adoptedRemark;
	}

	public String getAdoptedUserID() {
		return adoptedUserID;
	}

	public void setAdoptedUserID(String adoptedUserID) {
		this.adoptedUserID = adoptedUserID;
	}

	public String getAdoptedUserMark() {
		return adoptedUserMark;
	}

	public void setAdoptedUserMark(String adoptedUserMark) {
		this.adoptedUserMark = adoptedUserMark;
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
