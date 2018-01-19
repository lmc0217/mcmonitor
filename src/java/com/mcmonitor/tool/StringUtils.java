package com.mcmonitor.tool;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 字符串工具类
 * 
 * @author liu mengchao
 * 
 */
public final class StringUtils {
	//判断是否包含某字符
	public static boolean contains(String[] values, String key) {
		if (values == null || key == null) {
			return false;
		}
		for (String v : values) {
			if (key.equals(v)) {
				return true;
			}
		}
		return false;
	}
	//判断是否为空字符串
	public static boolean isEmpty(String str) {
		return (str == null || str.isEmpty() || str.equals("null"));
	}
	// 各数值转string
	public static String getString(int i) {
		return String.valueOf(i);
	}
	public static String getString(long l) {
		return String.valueOf(l);
	}
	public static String getString(float l) {
		return String.valueOf(l);
	}
	public static String getString(double d) {
		return String.valueOf(d);
	}
	
	public static String getString(boolean b) {
		if (b) {
			return "1";
		} else {
			return "0";
		}
	}
	// 字符串转sql string
	public static String getString(String d) {
		StringBuilder builder = new StringBuilder();
		builder.append("'").append(d).append("'");
		return builder.toString();
	}
	//时间转sql string
	public static String getString(Date date) {
		// return Tool.getDateFormat().format(date);
		if (date == null) {
			return "NULL";
		}
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
				DateFormat.MEDIUM, Locale.CHINA);
		StringBuilder builder = new StringBuilder();
		builder.append("'").append(format.format(date)).append("'");
		return builder.toString();
	}
	//时间转nstring
	public static String getStringDate(Date date) {

		if (date == null) {
			return "NULL";
		}
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,
				DateFormat.MEDIUM, Locale.CHINA);
		return format.format(date);
	}
}
