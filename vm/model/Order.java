package vm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;

public class Order implements Serializable{	
	private static final long serialVersionUID = 1L;
	
	private String customerID;
	private BigDecimal[] goodsID;
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
	public BigDecimal[] getGoodsID() {
		return goodsID;
	}
	public void setGoodsID(BigDecimal[] goodsID) {
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
		return "Order [customerID=" + customerID + ", goodsID=" + goodsID
				+ ", inputMoney=" + inputMoney + ", buyQuantity="
				+ Arrays.toString(buyQuantity) + ", totalAmount=" + totalAmount
				+ ", orderDate=" + orderDate + "]";
	}
	
}

/*Serializable: 這是用來允許類別的實例序列化，這意味著將對象的狀態轉換成字節流，以便稍後可以將其恢復為對象的副本。這通常用於將對象的狀態保存到文件或通過網絡傳輸。
 *static final long serialVersionUID = 1L: 這是一個類的唯一標識符，在序列化和反序列化過程中使用。如果在反序列化過程中找到不同的 serialVersionUID，則會拋出 InvalidClassException。
 */

//不使用 Serializable 接口：
//1.無法進行序列化和反序列化：如果您的類別沒有實現 Serializable 接口，那麼這個類別的實例將無法被序列化，也就是說無法轉換成字節流。這意味著您不能將對象狀態保存到文件或通過網絡傳輸。
//2.在需要序列化的環境中會報錯：如果嘗試在需要序列化的環境中使用這個類別（例如，Java RMI，HTTP Session），會導致 NotSerializableException。

//不指定 serialVersionUID：
//1.默認生成 serialVersionUID：如果沒有明確指定 serialVersionUID，Java 會基於類的各種屬性（例如，字段，方法，接口等）自動生成一個 serialVersionUID。這個生成過程是不穩定的，因為稍微修改類的實現（即使是無關緊要的改變，比如添加一個方法或字段）都會導致不同的 serialVersionUID。
//2.反序列化問題：如果一個類的 serialVersionUID 在序列化和反序列化之間發生了變化，將會導致 InvalidClassException。這種情況在類版本變更時尤其常見。為了避免這種問題，建議明確指定 serialVersionUID，這樣即使類的實現有變化，只要變化不影響對象的兼容性，仍然可以正確反序列化。