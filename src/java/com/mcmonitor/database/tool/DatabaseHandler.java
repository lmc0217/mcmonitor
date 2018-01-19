package com.mcmonitor.database.tool;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mcmonitor.database.sqltable.BaseTable;
import com.mcmonitor.tool.StringUtils;



/**
 *  数据库处理插件
 * @author liu mengchao
 * @createDate 2017.1.18
 */
public class DatabaseHandler {
	/**
	 * device 是否存在
	 * @param tableName
	 * @param mac_id
	 * @return
	 * @throws SQLException 
	 */
	public static boolean hasIntoDB(PreparedStatement stmt,String mac_id) throws SQLException{
		
		boolean bool=false;
		
		stmt.setString(1, mac_id);
		ResultSet set=stmt.executeQuery();
		
		bool=set.next();
		
		SqlUtils.closeSet(set);
		
		return bool;
		
	}
	
	/**
	 * updata data
	 * @param stmt
	 * @param table
	 * @param where
	 * @throws SQLException 
	 */
	public static void updata(Statement stmt, BaseTable table, String where,String[] fields,String tableName) throws SQLException {
		
		stmt.execute(s_update(table,where,fields,tableName));
	}
	
	public static String s_update(BaseTable table,String where,String[] fields,String tableName){
		String sql = getUpdateStr(table,fields,tableName);
		StringBuilder builder=new StringBuilder();
		builder.append(sql).append(where);
		String finalsql=builder.toString();
		return finalsql;
	}
	
	public static String getUpdateStr(BaseTable table,String[] fields,String tableName){
		if(fields == null || fields.length == 0){
			fields = null;
		}
		String[] fieldvalue = table.fieldValue();
		StringBuilder builder=new StringBuilder();
		builder.append("update ").append(tableName).append(" set ");
		int fieldnum = 0;
		for(int i = 0; i< fieldvalue.length; i+=2){
			String field = fieldvalue[i];
			// can't update mac_id, -- as general TODO
			if("mac_id".equals(field)){
				continue;
			}
			if(fields == null ||StringUtils.contains(fields,field)){
				if(fieldnum > 0){
					builder.append(",");
				}
				builder.append(field);
				builder.append("=");
				builder.append(fieldvalue[i+1]);
				fieldnum++;
			}
		}
		return builder.toString();
	}

	public static void insert(Statement stmt, BaseTable table,String tableName) throws SQLException {
		
		stmt.execute(s_insert(table, tableName));
	}
	
	public static String s_insert(BaseTable table, String tname){	// for data and datac
		String[] fieldvalue = table.fieldValue();
		StringBuilder builder=new StringBuilder();
		StringBuilder f_buBuilder=new StringBuilder();
		StringBuilder v_buBuilder=new StringBuilder();
		f_buBuilder.append("insert into ").append(tname).append("(");
		v_buBuilder.append(" values(");
		for(int i = 0; i< fieldvalue.length; i+=2){
			String field = fieldvalue[i];
			if(i > 0){
				f_buBuilder.append(",");
				v_buBuilder.append(",");
			}
			f_buBuilder.append(field);
			v_buBuilder.append(fieldvalue[i+1]);
			
		}
		f_buBuilder.append(")");
		v_buBuilder.append(")");
		return builder.append(f_buBuilder).append(v_buBuilder).toString();
	}
	
	/**
	 * has into db
	 * @param stmt
	 * @param table
	 * @param where
	 * @return
	 * @throws SQLException
	 */
	public static boolean hasintoDB(Statement stmt,String where,String tableName) throws SQLException {
		boolean bool=false;
		StringBuilder builder=new StringBuilder();
		String sql=builder.append("select id from ").append(tableName).append(where).toString();
		ResultSet set=stmt.executeQuery(sql);
		bool=set.next();
		SqlUtils.closeSet(set);
		return bool;
	}
	
	/**
	 * 存储、更新数据updata or insert
	 * @param stmt
	 * @param table
	 * @param where
	 * @throws SQLException 
	 */
	public static void updateinsert(Statement stmt, BaseTable table, String where,String[]fields,String tableName) throws SQLException {
		
		if (StringUtils.isEmpty(where)) {
			return;
		}else {
			if (hasintoDB(stmt,where,tableName)) {
				updata(stmt,table,where,fields,tableName);
			}else {
				insert(stmt, table,tableName);
			}
		}
	}
}
