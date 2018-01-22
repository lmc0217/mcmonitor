package com.mcmonitor.main;

import java.io.File;
import java.sql.SQLException;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.openfire.user.UserManager;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;

import com.mcmonitor.database.tool.SqlConnectionPool;
import com.mcmonitor.tool.MyEnvironment;

/**
 *  Packet 数据处理插件
 * @author liu mengchao
 * @createDate 2017.1.17
 */
public class MonitorPlugin implements PacketInterceptor, Plugin {
	private XMPPServer server;
	// main for intercpetorn
    private InterceptorManager interceptorManager;
    
	public MonitorPlugin() {  
		interceptorManager = InterceptorManager.getInstance();
    } 
	
	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		// TODO Auto-generated method stub
		server = XMPPServer.getInstance();
        System.out.println("MonitorPlugin----插件开启");
        // check environment
     	if (!MyEnvironment.mkEviroment()) {
     		System.exit(-1);
     	}
        // connect database(mysql...)
        try {
        	SqlConnectionPool.getDataSource();
        	System.out.println("init datasource success---加载mysql数据库成功");
		} catch (Exception e) {
			System.out.println("init datasource fail---加载mysql数据库失败");
			System.exit(-1);
		}
        // 初始化数据处理线程池
        PacketHandler.getInstance().initThreadPool();
        // 注册拦截侦听
        interceptorManager.addInterceptor(this);
        System.out.println(server.getServerInfo());
	}

	@Override
	public void destroyPlugin() {
		// TODO Auto-generated method stub
		try {
			SqlConnectionPool.getDataSource().close();
			System.out.println("数据库连接已断开成功!!!!!!!!!!");  
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("MonitorPlugin----插件关闭");  
	}

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
        PacketHandler.handelPacket(packet, incoming, processed, session);
	}
}
