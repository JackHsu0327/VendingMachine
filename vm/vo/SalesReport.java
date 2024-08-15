package vm.vo;


public class SalesReport {
		// 訂單編號
		private long orderID;
		// 顧客姓名
		private String customerName;
		// 購買日期
		private String orderDate;
		// 飲料名稱
		private String goodsName;
		// 商品金額(購買單價)
		private int goodsBuyPrice;
		// 購買數量
		private int buyQuantity;
		// 購買金額
		private int buyAmount;
		// 投入金額
		private int inputMoney;

		public long getOrderID() {
			return orderID;
		}

		public void setOrderID(long orderID) {
			this.orderID = orderID;
		}

		public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}

		public String getOrderDate() {
			return orderDate;
		}

		public void setOrderDate(String orderDate) {
			this.orderDate = orderDate;
		}

		public String getGoodsName() {
			return goodsName;
		}

		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}

		public int getGoodsBuyPrice() {
			return goodsBuyPrice;
		}

		public void setGoodsBuyPrice(int goodsBuyPrice) {
			this.goodsBuyPrice = goodsBuyPrice;
		}

		public int getBuyQuantity() {
			return buyQuantity;
		}

		public void setBuyQuantity(int buyQuantity) {
			this.buyQuantity = buyQuantity;
		}

		public int getBuyAmount() {
			return buyAmount;
		}

		public void setBuyAmount(int buyAmount) {
			this.buyAmount = buyAmount;
		}

		public int getInputMoney() {
			return inputMoney;
		}

		public void setInputMoney(int inputMoney) {
			this.inputMoney = inputMoney;
		}
		
		public SalesReport(int orderID, String customerName, String orderDate, String goodsName, int goodsBuyPrice, int buyQuantity, int buyAmount) {
		        this.orderID = orderID;
		        this.customerName = customerName;
		        this.orderDate = orderDate;
		        this.goodsName = goodsName;
		        this.goodsBuyPrice = goodsBuyPrice;
		        this.buyQuantity = buyQuantity;
		        this.buyAmount = buyAmount;
		}		 
		public SalesReport() {}

		@Override
		public String toString() {
			return "SalesReport [orderID=" + orderID + ", customerName="
					+ customerName + ", orderDate=" + orderDate + ", goodsName="
					+ goodsName + ", goodsBuyPrice=" + goodsBuyPrice
					+ ", buyQuantity=" + buyQuantity + ", buyAmount=" + buyAmount
					+ "]";
		}

		
	}
