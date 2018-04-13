package com.holly.collection.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestMySortProperies {
	public static void main(String[] args) throws IOException {
		InputStream is = TestMySortProperies.class.getClassLoader().getResourceAsStream("com/holly/collection/map/configtest.properties");
		MySortProperties properties = new MySortProperties();
		properties.load(is);
		Set<Object> set = properties.keySet();
		List<Object> fields = new ArrayList<Object>();
		for(Object s: set){
			fields.add(s);
		}
		System.out.println(fields.toString());
	}
}
