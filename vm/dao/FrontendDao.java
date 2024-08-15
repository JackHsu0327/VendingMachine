package vm.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import vm.model.Goods;


public class FrontendDao {

	private static FrontendDao frontendDao = new FrontendDao();
	
	private FrontendDao(){ }

	public static FrontendDao getInstance(){
		return frontendDao;
	}

	/**
     * 取得商品列表，根據頁碼和每頁大小進行分頁查詢。
     * @param pageNo 當前頁碼
     * @param pageSize 每頁顯示的商品數量
     * @return 包含當前頁面商品的列表
     */
	public List<Goods> getGoodsList(int pageNo, int pageSize) {
		List<Goods> goodsList = new ArrayList<>();
		String sql = "SELECT * FROM ( SELECT ROWNUM ROW_NUM, B.* FROM BEVERAGE_GOODS B WHERE ROWNUM <= ?) WHERE ROW_NUM > ?";
	    
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
		     PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			 int endRow = pageNo * pageSize;
	         int startRow = endRow - pageSize;			
			
	         stmt.setInt(1, endRow);
	         stmt.setInt(2, startRow);
		     
		     try (ResultSet rs = stmt.executeQuery()) {
		    	  while (rs.next()) {
		                Goods goods = new Goods();
		                goods.setGoodsID(rs.getLong("GOODS_ID"));
		                goods.setGoodsName(rs.getString("GOODS_NAME"));		                
		                goods.setGoodsPrice(rs.getInt("PRICE"));
		                goods.setGoodsQuantity(rs.getInt("QUANTITY"));
		                goods.setGoodsImageName(rs.getString("IMAGE_NAME"));
		                goods.setStatus(rs.getString("STATUS"));
		                goodsList.add(goods);
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return goodsList;
		}
	
	
	/**
	 * 搜尋關鍵字
	 * 並含有分頁功能
	 * 查出相應的商品並顯示出來
	 */
	public List<Goods> searchGoods(String searchKeyword, int pageNo, int pageSize) {
		 List<Goods> goodsList = new ArrayList<>();
		 String sql = "SELECT * FROM ( SELECT B.*, ROWNUM AS rn FROM BEVERAGE_GOODS B WHERE LOWER(B.GOODS_NAME) LIKE LOWER(?) AND ROWNUM <= ? ) WHERE rn > ?";
	        try (Connection conn = DBConnectionFactory.getOracleDBConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            int endRow = pageNo * pageSize;
	            int startRow = endRow - pageSize;

	            stmt.setString(1, "%" + searchKeyword + "%");
	            stmt.setInt(2, endRow);
	            stmt.setInt(3, startRow);

	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    Goods goods = new Goods();
	                    goods.setGoodsID(rs.getLong("GOODS_ID"));
	                    goods.setGoodsName(rs.getString("GOODS_NAME"));
	                    goods.setGoodsPrice(rs.getInt("PRICE"));
	                    goods.setGoodsQuantity(rs.getInt("QUANTITY"));
	                    goods.setGoodsImageName(rs.getString("IMAGE_NAME"));
	                    goods.setStatus(rs.getString("STATUS"));
	                    goodsList.add(goods);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return goodsList;	    
	}
	/**
	 * 取得符合搜尋條件的商品總數
	 *從DB中取出相應的商品數量
	 *或在無關鍵字的情境下調出所以商品的總數
	 */
	public int getGoodsCount(String searchKeyword) {
		 String sql = "SELECT COUNT(*) FROM BEVERAGE_GOODS";
		    if (searchKeyword != null && !searchKeyword.isEmpty()) {
		        sql += " WHERE LOWER(GOODS_NAME) LIKE LOWER(?)";
		    }
	    	    
	    try (Connection conn = DBConnectionFactory.getOracleDBConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        if (searchKeyword != null && !searchKeyword.isEmpty()) {
	            stmt.setString(1, "%" + searchKeyword + "%");
	        }

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return 0;
	}

	 /**
     * 根據商品ID查詢商品詳情。
     * @param goodsId 商品ID
     * @return 包含商品詳情的 Goods 對象
     */
	public Goods queryIdByGoods(long goodsId) {
        Goods goods = null;
        String querySQL = "SELECT * FROM BEVERAGE_GOODS WHERE GOODS_ID = ?";

        try (Connection conn = DBConnectionFactory.getOracleDBConnection();
             PreparedStatement stmt = conn.prepareStatement(querySQL)) {
            
            stmt.setLong(1, goodsId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    goods = new Goods();
                    goods.setGoodsID(rs.getLong("GOODS_ID"));
                    goods.setGoodsName(rs.getString("GOODS_NAME"));
                    goods.setGoodsPrice(rs.getInt("PRICE"));
                    goods.setGoodsQuantity(rs.getInt("QUANTITY"));
                    goods.setGoodsImageName(rs.getString("IMAGE_NAME"));
                    goods.setStatus(rs.getString("STATUS"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return goods;
    }
    /**
     * 更新商品庫存並新增訂單。如果庫存不足則會回滾事務。
     * @param cartGoods 購物車中的商品及其數量
     * @param customerID 顧客ID
     * @return 如果購買成功則返回 true，否則返回 false
     */
	public boolean updateGoodsQuantity(Map<Goods, Integer> cartGoods, String customerID) {
	    boolean purchaseSuccess = false;
	    try (Connection conn = DBConnectionFactory.getOracleDBConnection()) {
	        conn.setAutoCommit(false);
	        try {
	            // 更新商品庫存
	            for (Map.Entry<Goods, Integer> entry : cartGoods.entrySet()) {
	                Goods goods = entry.getKey();
	                int quantity = entry.getValue();

	                // 檢查庫存
	                String checkSQL = "SELECT QUANTITY FROM BEVERAGE_GOODS WHERE GOODS_ID = ?";
	                try (PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {
	                    checkStmt.setLong(1, goods.getGoodsID());
	                    try (ResultSet rs = checkStmt.executeQuery()) {
	                        if (rs.next()) {
	                            int currentQuantity = rs.getInt("QUANTITY");
	                            if (currentQuantity < quantity) {
	                                throw new SQLException("庫存不足");
	                            }
	                        }
	                    }
	                }

	                // 更新庫存
	                String updateSQL = "UPDATE BEVERAGE_GOODS SET QUANTITY = QUANTITY - ? WHERE GOODS_ID = ?";
	                try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
	                    updateStmt.setInt(1, quantity);
	                    updateStmt.setLong(2, goods.getGoodsID());
	                    updateStmt.executeUpdate();
	                }
	            }

	            // 新增訂單
	            String orderSQL = "INSERT INTO BEVERAGE_ORDER (ORDER_ID, ORDER_DATE, CUSTOMER_ID, GOODS_ID, GOODS_BUY_PRICE, BUY_QUANTITY) VALUES (BEVERAGE_ORDER_SEQ.NEXTVAL, SYSDATE, ?, ?, ?, ?)";
	            try (PreparedStatement orderStmt = conn.prepareStatement(orderSQL)) {
	                for (Map.Entry<Goods, Integer> entry : cartGoods.entrySet()) {
	                    Goods goods = entry.getKey();
	                    int quantity = entry.getValue();
	                    orderStmt.setString(1, customerID); // 客人id
	                    orderStmt.setLong(2, goods.getGoodsID());
	                    orderStmt.setInt(3, goods.getGoodsPrice());
	                    orderStmt.setInt(4, quantity);
	                    orderStmt.addBatch();
	                }
	                orderStmt.executeBatch();
	            }

	            conn.commit();
	            purchaseSuccess = true;
	        } catch (SQLException e) {
	            conn.rollback();
	            e.printStackTrace();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return purchaseSuccess;
	}
}
