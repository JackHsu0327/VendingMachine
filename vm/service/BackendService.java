package vm.service;

import java.util.List;
import java.util.Set;
import vm.dao.BackendDao;
import vm.model.Goods;
import vm.vo.SalesReport;

public class BackendService {
	
	private static BackendService backendService = new BackendService();
		
	private BackendService(){ }
	
	private BackendDao backEndDao = BackendDao.getInstance();

	public static BackendService getInstance() {
		return backendService;
	}
	
	//商品列表
	public Set<Goods> queryGoods(){
		return backEndDao.queryGoods();
	}
	
	//商品補貨作業
	public boolean updateGoods(Goods goods){
		
			
		
		return backEndDao.updateGoods(goods);
	}
	
	//商品新增上架
	public boolean addGoods(Goods goods){
		
		if (goods == null) {
            System.out.println("Goods object is null in BackendService.createGoods");
            return false;
        }
		return backEndDao.createGoods(goods);
	}
	//銷售報表
	public List<SalesReport> querySalesReport(String startDate,String endDate){
		
		return backEndDao.querySalesReport(startDate,endDate);
	}
	
	
	
 }
