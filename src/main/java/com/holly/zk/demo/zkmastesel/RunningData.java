package com.holly.zk.demo.zkmastesel;

import java.io.Serializable;

public class RunningData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5619199314045508002L;

	private long cid;
	private String serverName;

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

}
