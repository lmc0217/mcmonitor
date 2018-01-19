package com.mcmonitor.database.tool;

public class SqlRunnable implements Runnable {
private String sql;
	
	public SqlRunnable(String sql){
		this.sql = sql;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		SqlUtils.execute(sql);
	}

}
