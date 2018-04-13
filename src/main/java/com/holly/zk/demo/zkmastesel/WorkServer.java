package com.holly.zk.demo.zkmastesel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ExceptionUtil;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

public class WorkServer {
	private volatile boolean running = false;
	private ZkClient zkClient;
	private static final String MASTER_PATH = "/master";
	private IZkDataListener datalistener;
	private RunningData serverData;
	private RunningData masterData;	
	
	private ScheduledExecutorService delayExecutor = Executors.newScheduledThreadPool(1);
	private int delayTime = 5;
			
	
	WorkServer() {

	}

	WorkServer(RunningData rd) {
		this.serverData = rd;
		this.datalistener = new IZkDataListener() {
			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
				// TODO Auto-generated method stub
				//takeMaster();
				if(masterData!=null && masterData.getServerName().equals(serverData.getServerName())){
					takeMaster();
				}else{
					delayExecutor.schedule(new Runnable() {
						@Override
						public void run() {
							takeMaster();
							
						}
					}, delayTime, TimeUnit.SECONDS);
				}
			}
			
			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// TODO Auto-generated method stub
				
			}
		};
	}

	private void start() throws Exception {
		if(running){
			throw new Exception("server has startup");
		}
		running = true;
		//订阅数据改变事件
		zkClient.subscribeDataChanges(MASTER_PATH, datalistener);
		//争抢master
		takeMaster();
	}

	private void stop() throws Exception {
		if(!running){
			throw new Exception("server has shutdown");
		}
		running = false;
		zkClient.unsubscribeDataChanges(MASTER_PATH, datalistener);
		releaseMaster();
	}

	private void takeMaster() {
		if(!running){
			return;
		}
		//尝试创建master节点
		try {
			zkClient.create(MASTER_PATH, serverData, CreateMode.EPHEMERAL);
			masterData = serverData;
			
			//演示测试代码
			delayExecutor.schedule(new  Runnable() {
				public void run() {
					if(checkMaster()){
						releaseMaster();
					}
				}
			}, delayTime, TimeUnit.SECONDS);
			
			//
			
		} catch (ZkNodeExistsException e) {
			//集群中已经创建master,获取master节点数据
			RunningData  runningdata =zkClient.readData(MASTER_PATH,true);
			if(runningdata == null){
				takeMaster();
			}else{
				serverData = runningdata;
			}
			
		}catch (Exception e) {
			// TODO: handle exception ignore now
			
		}
		
		
	}

	private void releaseMaster() {
		if(checkMaster()){
			zkClient.delete(MASTER_PATH);
		}
	}
	private boolean checkMaster() {
		try {
			RunningData eventData = zkClient.readData(MASTER_PATH);
			masterData = eventData;
			if(masterData.getServerName().equals(serverData.getServerName())){
				return true;
			}
			return false;
		} catch (ZkNodeExistsException e) {
			return false;
		} catch (ZkInterruptedException e) {
			return checkMaster();
		}catch (Exception e) {
			return false;
		}
	}

}
