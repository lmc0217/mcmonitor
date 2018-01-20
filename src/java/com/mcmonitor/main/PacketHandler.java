package com.mcmonitor.main;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jivesoftware.openfire.session.Session;
import org.xmpp.packet.IQ;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;
import org.xmpp.packet.Presence;

import com.mcmonitor.database.sqltable.Thdata;
import com.mcmonitor.database.tool.DatabaseHandler;
import com.mcmonitor.database.tool.SqlRunnable;
import com.mcmonitor.database.tool.SqlUtils;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
/**
 * 包处理Handler类
 * 
 * @author liu mengchao
 * 
 */
public class PacketHandler {
	private ExecutorService packetDataTask;
	private static final PacketHandler packhandler = new PacketHandler();

	public static PacketHandler getInstance() {
		return packhandler;
	}
	
	private PacketHandler() {
		
	}
	
	/**
	 * 初始化线程池
	 */
	public void initThreadPool() {
		packetDataTask = Executors.newFixedThreadPool(100);
	}
	public ExecutorService getPacketDataTask() {
		return packetDataTask;
	}
	
	/**
	 * 统一处理拦截的packet
	 * 
	 */
	public static void handelPacket(Packet packet, boolean incoming, boolean processed, Session session) {
		Packet copyPacket = packet.createCopy();
		if (packet instanceof Message) {
			Message message = (Message) copyPacket;
			System.out.println("收到消息："+message.toXML());
		} else if (packet instanceof IQ) {
            IQ iq = (IQ) copyPacket;
            if (iq.getType() == IQ.Type.set && iq.getChildElement() != null && "session".equals(iq.getChildElement().getName())) {
            	System.out.println("用户登录成功："+ iq.toXML());
            }
        } else if (packet instanceof Presence) {
            Presence presence = (Presence) copyPacket;
            if (presence.getType() == Presence.Type.unavailable) {
            	System.out.println("用户退出服务器成功："+ presence.toXML());
            	Thdata thdata = new Thdata();
            	thdata.setMac_id("0d6764d2baf06644731d75fee9d3eaa8");
            	thdata.setDevice_id("0d6764d2baf06644731d75fee9d3eaa8");
            	thdata.setDatetime(new Timestamp(System.currentTimeMillis()));
            	thdata.setDevice_type(0);
            	thdata.setModel("湿湿度");
            	thdata.setStatus(1);
            	thdata.setTemp(2100);
            	thdata.setHumidity(5900);
            	thdata.setPrec(2);
            	thdata.setOrder_id(1);
            	SqlUtils.execute(new SqlRunnable(DatabaseHandler.s_insert(thdata,
        				"thdata")));
            } else {
            	System.out.println("收到presence数据包："+ presence.toXML());
            	parePacketXML(presence.toXML());
            }
        }
	}
	/**
	 * xml解析
	 * 
	 */
	private static void parePacketXML(String packetXML) {
		try {
			Document document = DocumentHelper.parseText(packetXML);
			HashMap<String, String> map = new HashMap<String, String>();  
			map.put( "xmn", "urn:xmpp:iot:data:save");  
			XPath xpath = document.createXPath( "//xmn:set/xmn:service");  
			xpath.setNamespaceURIs(map);
			
		    Element ele_set = document.getRootElement().element("set");
		    String xmlns = ele_set.getNamespaceURI();
		    if (xmlns.equals("urn:xmpp:iot:data:save")) {
		    	Node node_presence = document.selectSingleNode("/presence");
			    System.out.println("From = " + node_presence.valueOf("@from"));
			    Node node_set = xpath.selectSingleNode(document);
			    System.out.println("serviceName= " + node_set.valueOf("@name"));
			    System.out.println("serviceTimes= " + node_set.valueOf("@timestamp"));
			    
			    Node node_service = document.selectSingleNode("/presence/set/service/int[@name='temp']");
			    System.out.println("service name = " + node_service.valueOf("@name"));
			    
			    Attribute attribute = (Attribute)document.selectSingleNode("/presence/set[@xmlns]");
			    System.out.println("属性名：" + attribute.getName() + "--属性值："
	                    + attribute.getValue());
		    }
		} catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	 }
}
