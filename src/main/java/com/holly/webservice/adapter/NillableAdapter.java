package com.holly.webservice.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class NillableAdapter extends XmlAdapter<Object, Object>{

	@Override
	public Object unmarshal(Object v) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object marshal(Object v) throws Exception {
		if(v==null || "".equals(v)){
			v="";
		}
		return v;
	}

}
