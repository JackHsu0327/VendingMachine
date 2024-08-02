package vm.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import vm.model.Goods;

public class FrontendDao {

	private static FrontendDao frontendDao = new FrontendDao();
	
	private FrontendDao(){ }

	public static FrontendDao getInstance(){
		return frontendDao;
	}
//	前臺顧客登入查詢
//	public BuyGoodsRtn quertMemberByIdentificationNo(String identificationNo){
//		BuyGoodsRtn buyGoodsRtn = null;
//		
//		return buyGoodsRtn;
//	}
	//前臺顧客瀏灠商品
	public Set<Goods> searchGoods(String searchKeyword) {
		Set<Goods> goods = new LinkedHashSet<>();
		String querySQL = "SELECT * FROM BEVERAGE_GOODS WHERE GOODS_NAME IS NOT NULL AND GOODS_NAME LIKE ? AND STATUS IS NOT NULL";
		
		if(searchKeyword == null || searchKeyword.trim().isEmpty()){
//			System.out.println("Search keyword is empty or null, returning empty result set.");
			return goods;
		}
//		
		String keyword = "%" + searchKeyword.trim() + "%";
//		System.out.println("Search keyword after trimming and wrapping: '"+keyword+"'");
		
		try(Connection conn = DBConnectionFactory.getOracleDBConnection();
	 	PreparedStatement stmt = conn.prepareStatement(querySQL);){
			
			
			stmt.setString(1, keyword);
//			stmt.setString(1, "%" + searchKeyword + "%");
			
//			System.out.println("Executing query: " + querySQL);
//	        System.out.println("With parameter: " + keyword);
			
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()){
					Goods good = new Goods();
					good.setGoodsID(rs.getBigDecimal("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setGoodsImageName(rs.getString("IMAGE_NAME"));
					good.setStatus(rs.getString("STATUS"));
					goods.add(good);
				}				
			} catch(SQLException e){
				e.printStackTrace();
				throw e;
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return goods;
	}
	//查詢顧客所購買商品資料(價格、庫存)
//	public Map<BigDecimal, Goods> buyGoods(Set<BigDecimal> goodsIDs) 
//			throws SQLException {
//		Map<BigDecimal, Goods> goods = new LinkedHashMap<>();
//		String updateSQL ="UPDATE BEVERGE_GOODS SET QUANTITY = QUANTITY - ? WHERE GOODS_ID = ?";
//		String insertSQL ="INSERT INTO ORDERS (ORDER_ID, GOODS_ID, BUY_QUANTITY, BUY_PRICE)VALUES(?, ?, ?, ?)";
//		try(Connection conn = DBConnectionFactory.getOracleDBConnection();){
//			
//		conn.setAutoCommit(false);
//		try(PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
//			PreparedStatement insertStmt = conn.prepareStatement(insertSQL)){
//			
//			for(BigDecimal GoodsID: goodsIDs){
//				int buyQuantity = goodsQuantityMap.get(GoodsID);
//				
//				updateStmt.setInt(1, buyQuantity);
//				updateStmt.setBigDecimal(2, GoodsID);
//				updateStmt.addBatch();
//				
//				Goods good = new Goods();
//				SalesReport sr = new SalesReport();
//				int buyAmount = good.getGoodsPrice()*sr.getBuyQuantity();
//				
//				insertStmt.setLong(1, sr.setOrderID(););
//				
//				
//			}
//			
//			
//			
//			
//			
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return goods;
	
	//交易完成更新扣商品庫存數量
	public boolean batchUpdateGoodsQuantity(Set<Goods> goods){
		boolean updateSuccess = false;
		try(Connection conn = DBConnectionFactory.getOracleDBConnection();){
			conn.setAutoCommit(false);
			String updateSQL = "UPDATE BEVERAGE_GOODS SET GOODS_NAME = ?, PRICE = ?, QUANTITY = ?, IMAGE_NAME = ?, STATUS = ? WHERE GOODS_ID = ?";
			try(PreparedStatement stmt = conn.prepareStatement(updateSQL);){
				
				for(Goods good : goods) {
				stmt.setBigDecimal(1, good.getGoodsID());
				stmt.setString(2, good.getGoodsName());
				stmt.setInt(3, good.getGoodsPrice());
				stmt.setInt(4, good.getGoodsQuantity());
				stmt.setString(5, good.getGoodsImageName());
				stmt.setString(6, good.getStatus());;
				stmt.addBatch();
				}
				int[] batchUpdateCounts = stmt.executeBatch();
				conn.commit();
				 updateSuccess = true;
				
			} catch(SQLException e){
				conn.rollback();
				throw e;
			}			
		} catch(SQLException e){
			updateSuccess = false;
			e.printStackTrace();
		}
		return updateSuccess;	
	}
	//建立訂單資料
	public boolean batchCreateGoodsOrder(String customerID, Map<Goods,Integer> goodsOrders){
		boolean insertSuccess = false;
		try(Connection conn = DBConnectionFactory.getOracleDBConnection();){
			conn.setAutoCommit(false);
			String insertSQL = "INSERT INTO BEVERAGE_ORDER (ORDER_ID, ORDER_DATE, CUSTOMER_ID, GOODS_ID, GOODS_BUY_PRICE, BUY_QUANTITY) VALUES (BEVERAGE_ORDER_SEQ.NEXTVAL, SYSDATE, ?, ?, ?, ?)";
			try(PreparedStatement stmt = conn.prepareStatement(insertSQL)){
				
				for(Map.Entry<Goods, Integer> entry : goodsOrders.entrySet()){
					Goods goods = entry.getKey();
					int quantity = entry.getValue();
					
					stmt.setString(1, customerID);
					stmt.setBigDecimal(2, goods.getGoodsID());
					stmt.setInt(3, goods.getGoodsPrice());
					stmt.setInt(4, quantity);
					
					stmt.addBatch();
				}
				
				int[] batchInsertCounts = stmt.executeBatch();
				conn.commit();
				insertSuccess = true;
			} catch(SQLException e){
				if (conn != null)
				conn.rollback();
				throw e;
			}			
		} catch(SQLException e){
			insertSuccess = false;
			e.printStackTrace();
		}
		return insertSuccess;
	}	
}
