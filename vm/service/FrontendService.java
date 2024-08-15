package vm.service;


import java.util.List;
import java.util.Map;
import vm.dao.FrontendDao;
import vm.model.Goods;


public class FrontendService {

	private static FrontendService frontEndService = new FrontendService();
	
	private FrontendService(){}
	
	private FrontendDao frontendDao = FrontendDao.getInstance();
	
	public static FrontendService getInstance(){
		return frontEndService;
	}
	//商品列表
	public List<Goods> getGoodsList(int pageNo, int pageSize) {
	    return frontendDao.getGoodsList(pageNo, pageSize);
	}
	//搜尋關鍵字
	public List<Goods> searchGoods(String searchKeyword, int pageNo, int pageSize) {
	    return frontendDao.searchGoods(searchKeyword, pageNo, pageSize);
	}
	//取得符合搜尋條件的商品總數
	public int getGoodsCount(String searchKeyword) {
	    return frontendDao.getGoodsCount(searchKeyword);
	}
	//根據商品ID查詢商品詳情	
	public Goods queryIdByGoods(long goodsId) {
		
		return frontendDao.queryIdByGoods(goodsId);
	}
	//更新商品庫存並新增訂單
	public boolean updateGoodsQuantity(Map<Goods, Integer> cartGoods, String customerID, int inputMoney) {
	    if (cartGoods == null || cartGoods.isEmpty()) {
	        return false;
	    }
	    int totalPrice = 0;

	    for (Map.Entry<Goods, Integer> entry : cartGoods.entrySet()) {
	        Goods goods = entry.getKey();
	        Integer quantity = entry.getValue();
	        totalPrice += goods.getGoodsPrice() * quantity;
	    }

	    if (inputMoney < totalPrice) {
	        return false; // 金额不足
	    }
	    return frontendDao.updateGoodsQuantity(cartGoods, customerID);
	}
	
	

}
