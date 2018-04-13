package com.holly.util;

import java.util.UUID;

import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;

public class UUIDGenerator {
	private static int count = 0;

	public static String guid() {
		count++;
		String resultId = "" + System.currentTimeMillis() + "" + count;
		if (count >= 90) {
			count = 0;
		}
		return resultId;
	}
	public static String uuid(final String prefix){
		return prefix+UUID.randomUUID();
	}

	/**
	 * hibernate id生成器
	 * 
	 * @return
	 */
	public static String hibernateUUID() {
		IdentifierGenerator gen = new UUIDHexGenerator();
		String id = (String) gen.generate(null, null);
		return id;
	}

}
