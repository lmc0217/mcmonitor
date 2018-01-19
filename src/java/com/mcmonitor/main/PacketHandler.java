package com.mcmonitor.main;

import java.sql.Timestamp;
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
            }
        }
	}
}
