package vm.formbean;

import java.io.Serializable;
import java.util.Arrays;


import org.apache.struts.action.ActionForm;

public class GoodsOrderForm extends ActionForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private String customerID;
	private String[] goodsID;
	private int inputMoney;
	private String[] buyQuantity;
	private int totalAmount;
	private String orderDate;
	private String pageNo;
	private String searchKeyword;

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String[] getGoodsID() {
		return goodsID;
	}

	public void setGoodsID(String[] goodsID) {
		this.goodsID = goodsID;
	}

	public int getInputMoney() {
		return inputMoney;
	}

	public void setInputMoney(int inputMoney) {
		this.inputMoney = inputMoney;
	}

	public String[] getBuyQuantity() {
		return buyQuantity;
	}

	public void setBuyQuantity(String[] buyQuantity) {
		this.buyQuantity = buyQuantity;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	
	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	@Override
	public String toString() {
		return "GoodsOrderForm [customerID=" + customerID + ", goodsID="
				+ Arrays.toString(goodsID) + ", inputMoney=" + inputMoney
				+ ", buyQuantity=" + Arrays.toString(buyQuantity)
				+ ", totalAmount=" + totalAmount + ", orderDate=" + orderDate
				+ "]";
	}

}
