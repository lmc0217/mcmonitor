package com.mcmonitor.database.tool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

/**
 * 数据库连接池
 * 
 * @author liu mengchao
 * 
 */
public class SqlConnectionPool {

	private static DruidDataSource druidsource;// 数据源

	private final static String PATH_FILE = "/opt/openfire/conf";// file path
	private final static String PROP_FILE = "jdbc.properties";// file name
	/* 初始化数据源 */
	static {
		Properties properties = loadPropertyFile(PROP_FILE);

		try {
			druidsource = (DruidDataSource) DruidDataSourceFactory
					.createDataSource(properties);
		} catch (Exception e) {
		}

	}

	/**
	 * 获取数据源实例
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static DruidDataSource getDataSource() throws SQLException {
		return druidsource;
	}

	/**
	 * 获取数据库连接实例
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static DruidPooledConnection getConnection() throws SQLException {
		return druidsource.getConnection();
	}

	/**
	 * 加载配置文件
	 * 
	 * @param string
	 * @return
	 */
	private static Properties loadPropertyFile(String fileName) {
		String filepath;
		if (null == fileName || fileName.equals("")) {
			throw new IllegalArgumentException(
					"Properties file path can not be null : " + fileName);
		}
		filepath = PATH_FILE + File.separator + fileName;
		InputStream inputStream = null;
		Properties p = null;

		try {
			inputStream = new BufferedInputStream(new FileInputStream(filepath));
			p = new Properties();
			p.load(inputStream);
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Properties file not found: "
					+ fileName);
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Properties file can not be loading: " + fileName);
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;

	}
		
}
