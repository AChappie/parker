package com.hand.hsp.servicekpijira;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hand.hsp.utils.DBUtils;


// 获取Jira数据
public class GetJiraProject {

	// public static final String DRIVER = PropertyUtil.getProperty("DRIVER");
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	// public static final String URL = PropertyUtil.getProperty("URL");
	public static final String URL = "jdbc:mysql://localhost:3306/sdgp";
	// public static final String USERNAME =
	// PropertyUtil.getProperty("USERNAME");
	public static final String USERNAME = "sdgp";
	// public static final String PASSWORD =
	// PropertyUtil.getProperty("PASSWORD");
	public static final String PASSWORD = "sdgp";

	// Jira数据库
	public static final String DRIVERJira = "com.mysql.jdbc.Driver";
	// public static final String URL = PropertyUtil.getProperty("URL");
	public static final String URLJira = "jdbc:mysql://192.168.207.73:3306/sdgp";
	// public static final String USERNAME =
	// PropertyUtil.getProperty("USERNAME");
	public static final String USERNAMEJira = "sdgp";
	// public static final String PASSWORD =
	// PropertyUtil.getProperty("PASSWORD");
	public static final String PASSWORDJira = "sdgp";

	static Connection connJira = null;
	static Connection connHsp = null;
	static String getKeySql = "select id,pname from project";
	static String insertJiraSql = "insert into jira_project value(?,?)";

	static int jiraID = 0;
	static String jiraName = "";

	static PreparedStatement getKeyPs = null;
	static PreparedStatement insertPs = null;
	static ResultSet resultSet = null;
	static PreparedStatement checkPs = null;

	public static void initConnJira() throws SQLException, ClassNotFoundException {
		connJira = DBUtils.getJDBCConnection(DRIVERJira, URLJira, USERNAMEJira, PASSWORDJira);
	}

	public static void initConnHsp() throws SQLException, ClassNotFoundException {
		connHsp = DBUtils.getJDBCConnection(DRIVER, URL, USERNAME, PASSWORD);
	}

	public static void putToMsql(int jiraID, String jiraName) throws SQLException, ClassNotFoundException {

		if (connJira == null) {
			initConnHsp();
		}

		String checkSql = "select count(1) from project where id = '" + jiraID + "'";

		checkPs = connJira.prepareStatement(checkSql);
		ResultSet upResult = checkPs.executeQuery();
		while (upResult.next()) {
			if (upResult.getInt(1) == 0) {
				insertPs = connJira.prepareStatement(insertJiraSql);

				insertPs.setInt(1, jiraID);
				insertPs.setString(2, jiraName);
				insertPs.executeUpdate();
			} else {
				System.out.println("没有新的Jira数据产生");
			}
		}

	}

	public static void getAllKey() throws ClassNotFoundException, SQLException {
		if (connHsp == null) {
			initConnJira();
		}
		getKeyPs = connHsp.prepareStatement(getKeySql);
		resultSet = getKeyPs.executeQuery();
		while (resultSet.next()) {
			jiraID = resultSet.getInt("id");
			jiraName = resultSet.getString("pname");

			putToMsql(jiraID, jiraName);

		}

	}

	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		System.out.println("GetJiraProject===start");
		getAllKey();
		System.out.println("GetJiraProject===end");
	}

}
