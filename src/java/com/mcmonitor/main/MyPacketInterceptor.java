package com.mcmonitor.main;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.UserManager;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

import com.mcmonitor.log.Log;

public class MyPacketInterceptor implements PacketInterceptor {
	
	/**
	 *  Packet拦截
	 */
	@Override
	public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed)
			throws PacketRejectedException {
		// TODO Auto-generated method stub
		JID recipient = packet.getTo();
        if (recipient != null) {
            String username = recipient.getNode();
            // 广播消息或是不存在/没注册的用户.
            if (username == null || !UserManager.getInstance().isRegisteredUser(recipient)) {
                return;
            } else if (!XMPPServer.getInstance().getServerInfo().getXMPPDomain().equals(recipient.getDomain())) {
            	System.out.println("不是当前openfire服务器的信息");  
                return;
            } else if ("".equals(recipient.getResource())) {
            }
        }
        if (incoming && !processed) {
        	PacketHandler.handelPacket(packet, incoming, processed, session);
		} else {
//			Log.err.info("服务器处理过的或发出的packet，不拦截");
		}
	}
}
