package vm.vo;

import java.util.Set;

public class ShoppingCartGoodsInfo {

	private int totalAmount;
	
	private Set<ShoppingCartGoods> shoppingCartGoods;

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Set<ShoppingCartGoods> getShoppingCartGoods() {
		return shoppingCartGoods;
	}

	public void setShoppingCartGoods(Set<ShoppingCartGoods> shoppingCartGoods) {
		this.shoppingCartGoods = shoppingCartGoods;
	}	
}
