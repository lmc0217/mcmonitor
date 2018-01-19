package com.mcmonitor.database.sqltable;

/**
 * 数据库表基类
 * 
 * @author liu mengchao
 * 
 */
public abstract class BaseTable {
	/**
	 * 映射表名
	 * @return
	 */
	public String getTableName(){
		return this.getClass().getSimpleName();
	}
	//映射表字段
	public abstract String[] fieldValue();
}
