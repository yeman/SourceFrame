package com.holly.collection.map;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class MySortProperties extends Properties {
	private LinkedHashSet keys = new LinkedHashSet();

	@Override
	public Set<String> stringPropertyNames() {
		LinkedHashSet set = new LinkedHashSet();
	    for(Object key: keys){
	    	set.add(key);
	    }
		return set;
	}

	@Override
	public synchronized Enumeration<Object> keys() {
		return Collections.<Object>enumeration(keys);
	}

	@Override
	public synchronized Object put(Object key, Object value) {
		keys.add(key);
		return super.put(key, value);
	}

	@Override
	public Set<Object> keySet() {
		return keys;
	}

}
