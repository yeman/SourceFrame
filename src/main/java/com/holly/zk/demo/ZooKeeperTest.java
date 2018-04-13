package com.holly.zk.demo;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

public class ZooKeeperTest {
	
	
	@Test
	public void testApi() throws IOException, KeeperException, InterruptedException{
		ZooKeeper zk = new ZooKeeper("192.168.101.200:2181",10000, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("触发 "+event.getPath()+" "+event.getType()+"事件");
				// TODO Auto-generated method stub
			}
		});
	  
	  //创建一个目录节点
	  zk.create("/rootpath", "something".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		
	 //创建一个目录节点
	  zk.create("/rootpath/child", "something-children".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	  //getData
	  System.out.println(new String(zk.getData("/rootpath",false,null)));
	  //exits
	  System.out.println(zk.exists("/rootpath/child", true));
	  
	  //getChildren
	  System.out.println(zk.getChildren("/rootpath", true));
	  
	  //修改节点数据
	  zk.setData("/rootpath/child", "something-children-modified".getBytes(), -1);
	  
	  //创建另外一个节点
	  zk.create("/rootpath/child2", "something-children2".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
	  //获取children
	  List str = zk.getChildren("/rootpath", true);
	  System.out.println(str);
	  
	  //删除节点
	  zk.delete("/rootpath/child", -1);
	  zk.delete("/rootpath/child2", -1);
	  zk.delete("/rootpath", -1);
	  
	  
	  //关闭连接
	  zk.close();
	}
	
	
}
