package com.mcmonitor.database.sqltable;

import java.sql.Timestamp;

import com.mcmonitor.tool.Constant;
import com.mcmonitor.tool.StringUtils;

/**
 * 温湿度映射表
 * 
 * @author liu mengchao
 * =============================
 * CREATE TABLE `thdata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mac_id` varchar(34) NOT NULL,
  `device_id` varchar(34) DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  `model` varchar(34) DEFAULT NULL,
  `status` int(2) DEFAULT -1,
  `device_type` int(2) DEFAULT 0,
  `order_id` int(4) DEFAULT NULL,
  `temp` int(11) NOT NULL DEFAULT '-1',
  `humidity` int(11) NOT NULL DEFAULT '-1',
  `prec` int(2) DEFAULT 2,
  PRIMARY KEY (`id`),
  KEY `datetime` (`datetime`),
  KEY `index1` (`mac_id`,`device_id`,`datetime`)
)
==========================================
 */
public class Thdata extends BaseTable{
	private String mac_id; // 硬件ID，中间件
	private Timestamp datetime;
	private String device_id = "";// 设备ID
	private String model = "";// 设备model
	private int order_id = -1;// 设备序列号id
	private int status = Constant.INT_DEFAULT_SQL_STATUS;
	private int temp = Constant.INT_DEFAULT_SQL_DATA;// 温度
	private int humidity = Constant.INT_DEFAULT_SQL_DATA;// 湿度
	private int device_type = 0; // 0->温湿度；1->单温度；2->单湿度
	private int prec = 2; // 表数据存储精度
	
	public String getMac_id() {
		return mac_id;
	}

	public void setMac_id(String mac_id) {
		this.mac_id = mac_id;
	}

	public Timestamp getDatetime() {
		return datetime;
	}

	public void setDatetime(Timestamp datetime) {
		this.datetime = datetime;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTemp() {
		return temp;
	}

	public void setTemp(int temp) {
		this.temp = temp;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getDevice_type() {
		return device_type;
	}

	public void setDevice_type(int device_type) {
		this.device_type = device_type;
	}

	public int getPrec() {
		return prec;
	}

	public void setPrec(int prec) {
		this.prec = prec;
	}

	@Override
	public String[] fieldValue() {
		// TODO Auto-generated method stub
		String[] fieldvalue = {
				"mac_id",StringUtils.getString(mac_id),
				"device_id",StringUtils.getString(device_id),
				"model",StringUtils.getString(model),
				"datetime",StringUtils.getString(datetime),
				"order_id",StringUtils.getString(order_id),
				"temp",StringUtils.getString(temp),
				"humidity",StringUtils.getString(humidity),
				"status",StringUtils.getString(status),
				"device_type",StringUtils.getString(device_type),
				"prec",StringUtils.getString(prec),
			};
		return fieldvalue;
	}

}
