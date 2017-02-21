package com.hand.hsp.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBUtils implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static int getTablePK(String sequenceName) {
		String sql = "SELECT " + sequenceName + ".nextval FROM dual";
		
		Connection conn = null;
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		int result = -1;
		try {
			conn = getJNDIConnection();
			ptmt = conn.prepareStatement(sql);
			rs = ptmt.executeQuery(sql);
			if (rs.next())
				result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			close(conn, ptmt, rs);
		}
		return result;
	}

	public static Connection getJDBCConnection(String driver, String url, String username, String password) {
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getJNDIConnection(String dataSource) {
		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = null;
			if (ctx != null) {
				Object o = ctx.lookup(dataSource);
				ds = (DataSource) o;
			}
			return ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Connection getJNDIConnection() {
		return getJNDIConnection("java:comp/env/jdbc/HTSC");
	}

	public static void close(Connection conn, PreparedStatement stat, ResultSet rs) {
		try {
			if (conn != null) {
				conn.close();
			}
			if (stat != null) {
				stat.close();
			}
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}