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
			map.put( "save", "urn:xmpp:iot:data:save");
			XPath xpath_service = document.createXPath( "//save:set/save:service");
			xpath_service.setNamespaceURIs(map);
			XPath xpath_data_temp = document.createXPath( "//save:set/save:service//save:int[@name='temp']");
			xpath_data_temp.setNamespaceURIs(map);
			XPath xpath_data_humi = document.createXPath( "//save:set/save:service//save:int[@name='humi']");
			xpath_data_humi.setNamespaceURIs(map);
			XPath xpath_data_prec = document.createXPath( "//save:set/save:service//save:int[@name='digit']");
			xpath_data_prec.setNamespaceURIs(map);
			
		    Element ele_set = document.getRootElement().element("set");
		    String xmlns = ele_set.getNamespaceURI();
		    if (xmlns.equals("urn:xmpp:iot:data:save")) {
		    	Node node_presence = document.selectSingleNode("/presence");
//			    System.out.println("From = " + node_presence.valueOf("@from"));
//			    System.out.println("MacID = " + node_presence.valueOf("@from").split("@")[0]);
			    
			    Node node_service = xpath_service.selectSingleNode(document);
//			    System.out.println("serviceName= " + node_service.valueOf("@name"));
//			    System.out.println("serviceTimes= " + node_service.valueOf("@timestamp"));
			    
			    Node node_data_temp = xpath_data_temp.selectSingleNode(document);
			    Node node_data_humi = xpath_data_humi.selectSingleNode(document);
			    Node node_data_prec = xpath_data_prec.selectSingleNode(document);
//			    System.out.println(node_data_temp.valueOf("@name") + " = " + node_data_temp.valueOf("@value"));
//			    System.out.println(node_data_humi.valueOf("@name") + " = " + node_data_humi.valueOf("@value"));
//			    System.out.println(node_data_prec.valueOf("@name") + " = " + node_data_prec.valueOf("@value"));
			    
			    Thdata thdata = new Thdata();
            	thdata.setMac_id(node_presence.valueOf("@from").split("@")[0]);
            	thdata.setDevice_id(node_presence.valueOf("@from").split("@")[0]);
            	thdata.setDatetime(new Timestamp(Long.parseLong(node_service.valueOf("@timestamp"))));
            	thdata.setDevice_type(0);
            	thdata.setModel("湿湿度");
            	thdata.setStatus(1);
            	thdata.setTemp(Integer.parseInt(node_data_temp.valueOf("@value")));
            	thdata.setHumidity(Integer.parseInt(node_data_humi.valueOf("@value")));
            	thdata.setPrec(Integer.parseInt(node_data_prec.valueOf("@value")));
            	thdata.setOrder_id(1);
            	SqlUtils.execute(new SqlRunnable(DatabaseHandler.s_insert(thdata,
        				"thdata")));
		    }
		} catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	 }
}
