package com.mcmonitor.database.tool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.mcmonitor.main.PacketHandler;

/**
 *  数据库操作方法类
 * @author liu mengchao
 * @createDate 2017.1.18
 */
public class SqlUtils {
	//数据外理线程添加线程池
	public static void execute(Runnable runnable) {

		if (null != runnable) {

			PacketHandler.getInstance().getPacketDataTask().submit(runnable);
		}

	}
	public static void execute(String sql) {
		DruidPooledConnection conn = null;
		Statement stmt = null;
		try {
			conn = SqlConnectionPool.getConnection();
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {

//			Log.sql.info(e.toString());
		} finally {
			closeStmt(stmt);
			closeConn(conn);
		}

	}
	
	//close sql
	public static void closeStmt(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
//				Log.sql.info(e.toString());
			}
		}
	}

	public static void closeConn(DruidPooledConnection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
//				Log.sql.info(e.toString());
			}
		}
	}

	public static void closeSet(ResultSet set) {
		if (set != null) {
			try {
				set.close();
			} catch (Exception e) {
//				Log.sql.info(e.toString());
			}
		}
	}
}
