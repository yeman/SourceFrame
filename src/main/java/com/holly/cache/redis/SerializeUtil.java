package com.holly.cache.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import lombok.extern.log4j.Log4j;
/**
 * TODO: 序列化工具类
 * date: 2017年11月17日
 */
@Log4j
public class SerializeUtil {
	/**
	 * @Description: 对象序列化为字节数组
	 * @param @param obj
	 * @param @return 
	 * @return byte[]
	 * @throws
	 */
	public static byte[] Serialize(Object obj) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			return baos.toByteArray();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}finally {
			if(baos!=null){
				try {
					baos.close();
				} catch (IOException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
			if(oos!=null){
				try {
					oos.close();
				} catch (IOException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return null;
		
	}
	
	/**
	 * 
	 * @Description: 字节数组序列化成对象
	 * @param @param buf
	 * @param @return 
	 * @return Object
	 * @throws
	 */
	public static Object unSerialize(byte[] buf) {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(buf);
			ois = new ObjectInputStream(bis);
			return ois.readObject();
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}finally {
			if(bis!=null){
				try {
					bis.close();
				} catch (IOException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
			if(ois!=null){
				try {
					ois.close();
				} catch (IOException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return null;

	}
}
