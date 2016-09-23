package com.dev.core.model;

/*
 * Ö§¸¶·½Ê½
 * */
public class StorePayMModel {
	public String paymentKeyName = "";
	public String paymentKeyCount = "";
	public String subSamount = "";
	public String payType = "";
	public String subNetAmount = "";
	
	public String getSubNetAmount() {
		return subNetAmount;
	}
	public void setSubNetAmount(String subNetAmount) {
		this.subNetAmount = subNetAmount;
	}
	public String getPaymentKeyName() {
		return paymentKeyName;
	}
	public void setPaymentKeyName(String paymentKeyName) {
		this.paymentKeyName = paymentKeyName;
	}
	public String getPaymentKeyCount() {
		return paymentKeyCount;
	}
	public void setPaymentKeyCount(String paymentKeyCount) {
		this.paymentKeyCount = paymentKeyCount;
	}
	public String getSubSamount() {
		return subSamount;
	}
	public void setSubSamount(String subSamount) {
		this.subSamount = subSamount;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}

}
