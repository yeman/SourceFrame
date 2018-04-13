package com.holly.analyzer.elasticsearch;

/*import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Properties;

import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.LocalTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;*/

import com.google.common.collect.Lists;

public class Cluster {/*
	
	public static  List<TransportAddress> getAllInetSocketTransportAddress(InputStream is) throws IOException{
		Properties p = new Properties();
		p.load(is);
		return null;
	}
	public static  List<TransportAddress> getAllInetSocketTransportAddress(List<NodeInfo> nodeInfo){
		List<LocalTransportAddress> list = Lists.newArrayList();
		for(NodeInfo node:nodeInfo){
			InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(
													new InetSocketAddress(node.getAddress(), Integer.parseInt(node.getPort())));
			//list.add(inetSocketTransportAddress);
		}
		return null;
	}
	
	
	class NodeInfo{
		private String address;
		private String port;
		
		NodeInfo(){
		}
		
		public NodeInfo(String address, String port) {
			super();
			this.address = address;
			this.port = port;
		}

		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getPort() {
			return port;
		}
		public void setPort(String port) {
			this.port = port;
		}
		
	}
*/}
