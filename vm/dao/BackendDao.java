package vm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;



import vm.model.Goods;
import vm.model.Member;
import vm.vo.SalesReport;

public class BackendDao {

	private static BackendDao backEndDao = new BackendDao();
	
	private BackendDao(){ }

	public static BackendDao getInstance(){
		return backEndDao;
	}
	
	// 商品列表
	public Set<Goods> queryGoods(){
		Set<Goods> goods = new LinkedHashSet<>();
		String querySQL ="SELECT GOODS_ID, GOODS_NAME, PRICE, QUANTITY, IMAGE_NAME, STATUS FROM BEVERAGE_GOODS";
		try(Connection conn = DBConnectionFactory.getOracleDBConnection();
		 	PreparedStatement stmt = conn.prepareStatement(querySQL);
			ResultSet rs = stmt.executeQuery()){
			while(rs.next()){
				Goods good = new Goods();
				good.setGoodsID(rs.getLong("GOODS_ID"));
				good.setGoodsName(rs.getString("GOODS_NAME"));
				good.setGoodsPrice(rs.getInt("PRICE"));
				good.setGoodsQuantity(rs.getInt("QUANTITY"));
				good.setGoodsImageName(rs.getString("IMAGE_NAME"));
				good.setStatus(rs.getString("STATUS"));
				goods.add(good);
			}				
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return goods;
	}
	
	// 商品補貨作業
	public boolean updateGoods(Goods goods){
		boolean updateSuccess = false;

		// Step1:取得Connection
		try(Connection conn = DBConnectionFactory.getOracleDBConnection();){
			// 設置交易不自動提交
			conn.setAutoCommit(false);
			// Update SQL
			String querySQL ="SELECT QUANTITY, PRICE FROM BEVERAGE_GOODS WHERE GOODS_ID =?";
			int queryQUA = 0;
			int quertPrice = 0;
			try(PreparedStatement stmt = conn.prepareStatement(querySQL)){
				stmt.setLong(1, goods.getGoodsID());	
				try(ResultSet rs = stmt.executeQuery()){
					if (rs.next()) {
					queryQUA = rs.getInt("QUANTITY");
					quertPrice = rs.getInt("PRICE");
					
                	}
				}
			}
			// 計算新的庫存量
			int newQUA = queryQUA + goods.getGoodsQuantity();
			
			// 如果價格為0且狀態為下架，保留原始價格
			int newPRI = goods.getGoodsPrice() == 0 && goods.getStatus().equals("下架") ? quertPrice :  goods.getGoodsPrice();
			
			String updateSQL = "UPDATE BEVERAGE_GOODS SET PRICE = ?, QUANTITY = ?, STATUS = ? WHERE GOODS_ID =?";
			// Step2:Create prepareStatement For SQL
			try(PreparedStatement stmt = conn.prepareStatement(updateSQL)){
				// Step3:將"資料欄位編號"、"資料值"作為引數傳入				
						
				stmt.setInt(1, newPRI);
				stmt.setInt(2, newQUA);
				stmt.setString(3, goods.getStatus());		
				stmt.setLong(4, goods.getGoodsID());
				
				// Step4:Execute SQL
				int recordCount = stmt.executeUpdate();
				updateSuccess = (recordCount > 0) ? true : false;
				
				conn.commit();
				
			} catch(SQLException e){
				// 若發生錯誤則資料 rollback(回滾)
				conn.rollback();
				throw e;
			}			
		} catch(SQLException e){
			updateSuccess =false;
			e.printStackTrace();
		}
		return updateSuccess;
	}
	
	// 商品新增上架
	public boolean createGoods(Goods goods){
		boolean createSuccess = false;
		// Step1:取得Connection
		try(Connection conn = DBConnectionFactory.getOracleDBConnection();){
			// 設置交易不自動提交
			conn.setAutoCommit(false);			
			// Insert SQL
			String insertSQL = "INSERT INTO BEVERAGE_GOODS (GOODS_ID, GOODS_NAME, PRICE, QUANTITY, IMAGE_NAME, STATUS) VALUES (BEVERAGE_GOODS_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";
			// Step2:Create prepareStatement For SQL
			try(PreparedStatement stmt = conn.prepareStatement(insertSQL)){
				
				 System.out.println("Preparing to insert goods: " + goods);
				// Step3:將"資料欄位編號"、"資料值"作為引數傳入
				stmt.setString(1, goods.getGoodsName());
				stmt.setInt(2, goods.getGoodsPrice());
				stmt.setInt(3, goods.getGoodsQuantity());
				stmt.setString(4, goods.getGoodsImageName());
				stmt.setString(5, goods.getStatus());
								
				// Step4:Execute SQL
				int recordCount = stmt.executeUpdate();
				createSuccess = (recordCount >0) ? true : false;
				
				// Step5:Transaction commit(交易提交)
				conn.commit();
				
				 System.out.println("Insert successful, recordCount: " + recordCount);
			} catch(SQLException e){
				// 若發生錯誤則資料 rollback(回滾)
				conn.rollback();
				throw e;
			}			
		} catch(SQLException e){
			createSuccess =false;
			e.printStackTrace();
		}
		return createSuccess;
	}
	
	// 銷售報表
	public List<SalesReport> querySalesReport(String startDate, String endDate) {
		List<SalesReport> reports = new ArrayList<>();
		
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("SELECT O.ORDER_ID, M.CUSTOMER_NAME, O.ORDER_DATE, G.GOODS_NAME, "); 
	    querySQL.append("O.GOODS_BUY_PRICE, O.BUY_QUANTITY, (O.GOODS_BUY_PRICE * O.BUY_QUANTITY) BUY_AMOUNT ");
	    querySQL.append("FROM BEVERAGE_ORDER O, BEVERAGE_MEMBER M, BEVERAGE_GOODS G ");
	    querySQL.append("WHERE O.CUSTOMER_ID = M.IDENTIFICATION_NO AND O.GOODS_ID = G.GOODS_ID ");
	    querySQL.append("AND O.ORDER_DATE BETWEEN TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') AND TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')");
	     
		
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
		         PreparedStatement stmt = conn.prepareStatement(querySQL.toString());){
			
			stmt.setString(1, startDate);
	        stmt.setString(2, endDate);     
			
			try(ResultSet rs = stmt.executeQuery()){	
			while(rs.next()){
				SalesReport report = new SalesReport();
				report.setOrderID(rs.getLong("ORDER_ID"));
				report.setCustomerName(rs.getString("CUSTOMER_NAME"));
				report.setOrderDate(rs.getString("ORDER_DATE"));
				report.setGoodsName(rs.getString("GOODS_NAME"));
				report.setGoodsBuyPrice(rs.getInt("GOODS_BUY_PRICE"));
				report.setBuyQuantity(rs.getInt("BUY_QUANTITY"));
				report.setBuyAmount(rs.getInt("BUY_AMOUNT"));
				reports.add(report);	
						
				}				
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}		
		return reports;
	}
	
	//會員登入
	public Member memberLogin(String identificationNo, String password) throws SQLException{
		Member memberLogin = null;
		String loginSQL = "SELECT * FROM BEVERAGE_MEMBER WHERE IDENTIFICATION_NO = ? AND PASSWORD = ?";
		
		try(Connection conn = DBConnectionFactory.getOracleDBConnection();
		PreparedStatement stmt = conn.prepareStatement(loginSQL)){
				// 設置參數
				stmt.setString(1, identificationNo);
				stmt.setString(2, password);
			try(ResultSet rs = stmt.executeQuery()){	
				if(rs.next()){
					memberLogin = new Member();
					memberLogin.setIdentificationNo(rs.getString("IDENTIFICATION_NO"));
					memberLogin.setPassword(rs.getString("PASSWORD"));
					memberLogin.setCustomerName(rs.getString("CUSTOMER_NAME"));
				}
			}catch (SQLException e) {
				e.printStackTrace();
		}
		
		return memberLogin;		
	   }
	}		
}		