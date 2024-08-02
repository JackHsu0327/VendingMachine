package vm.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import vm.dao.FrontendDao;
import vm.model.Goods;

public class FrontendService {

	private static FrontendService frontEndService = new FrontendService();
	
	private FrontendService(){}
	
	private FrontendDao frontendDao = FrontendDao.getInstance();
	
	public static FrontendService getInstance(){
		return frontEndService;
	}
	
	//前臺顧客瀏灠商品
	public Set<Goods> searchGoods(String searchKeyword) {
		
		return frontendDao.searchGoods(searchKeyword);
	}
	//查詢顧客所購買商品資料(價格、庫存)
	public Map<BigDecimal, Goods> buyGoods(Set<BigDecimal> goodsIDs , Goods Good){
				
		return frontendDao.buyGoods(goodsIDs, Good);
	}
	//交易完成更新扣商品庫存數量
	public boolean batchUpdateGoodsQuantity(Set<Goods> goods){
			
		return frontendDao.batchUpdateGoodsQuantity(goods);	
	}
	//建立訂單資料
	public boolean batchCreateGoodsOrder(String customerID, Map<Goods,Integer> goodsOrders){
			
		return frontendDao.batchCreateGoodsOrder(customerID, goodsOrders);
		}
	
}
