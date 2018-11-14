package dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

public class DBConnection {
	private static Connection connection;
	private Statement statement;

	public DBConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			if(connection == null || connection.isClosed())
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/vsygame", "vsyuser", "vsyPasswort18!");
			this.statement = connection.createStatement();
		}catch(CommunicationsException e){
			// TODO Auto-generated catch block
			e.printStackTrace();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String execute(String query){
		try {
			ResultSet rs = this.statement.executeQuery(query);
			String ret = "";
			while (rs.next()){
				ret += rs.getString(1);
				ret += " " + rs.getString(2);
				ret += " " + rs.getString(3);
				ret += " " + rs.getString(4);
				ret += "\n";
			}
			return ret;
		} catch (SQLException e) {
			return e.getMessage();
		}
	}
}
