package vm.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class DBConnectionFactory {

	public static Connection getOracleDBConnection(){
		Connection connection = null;
		try{
			Context ctx = new InitialContext();
			DataSource dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");
			connection = dataSource.getConnection();
		} catch (NamingException | SQLException e){
			e.printStackTrace();
		}
		
		return connection;
	}
}
