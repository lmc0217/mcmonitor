package com.mcmonitor.main;

import java.io.File;
import java.sql.SQLException;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;

import com.mcmonitor.database.tool.SqlConnectionPool;
import com.mcmonitor.log.Log;
import com.mcmonitor.tool.MyEnvironment;

/**
 *  Packet 数据处理插件
 * @author liu mengchao
 * @createDate 2017.1.17
 */
public class MonitorPlugin implements Plugin {
	private XMPPServer server;
	// main for InterceptorManager
    private InterceptorManager interceptorManager = InterceptorManager.getInstance();
    private PacketInterceptor packetInterceptor = null;
    
	public MonitorPlugin() {
		
    } 
	
	@Override
	public void initializePlugin(PluginManager manager, File pluginDirectory) {
		// TODO Auto-generated method stub
		server = XMPPServer.getInstance();
        Log.err.info("MonitorPlugin----插件开启");
        // check environment
     	if (!MyEnvironment.mkEviroment()) {
     		System.exit(-1);
     	}
        // connect database(mysql...)
        try {
        	SqlConnectionPool.getDataSource();
		} catch (Exception e) {
			Log.err.info("init datasource fail---加载mysql数据库失败");
			System.exit(-1);
		}
        // 初始化数据处理线程池
        PacketHandler.getInstance().initThreadPool();
        // 注册拦截侦听
        packetInterceptor = new MyPacketInterceptor();
        interceptorManager.addInterceptor(packetInterceptor);
        System.out.println(server.getServerInfo());
	}

	@Override
	public void destroyPlugin() {
		// TODO Auto-generated method stub
		if(packetInterceptor != null){  
			interceptorManager.removeInterceptor(packetInterceptor);
			Log.err.info("移除packet拦截器！！！！！！");
        } 
		try {
			SqlConnectionPool.getDataSource().close();
			Log.err.info("断开数据库连接！！！！！！");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.err.info("MonitorPlugin----插件移除");
	}
}
