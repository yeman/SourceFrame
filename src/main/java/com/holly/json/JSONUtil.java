package com.holly.json;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.holly.activeMQ.MessageFeature;
import com.holly.activeMQ.MessageType;
import com.holly.activeMQ.bean.Message;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JSONUtil {

	/**
	 * 将对象序列化成json文本
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJsonString(Object obj) {
		JSONObject jsonObj= JSONObject.fromObject(obj);
		return jsonObj.toString();
	}

	/**
	 * 将list对象序列化成json格式数据
	 * 
	 * @param list
	 * @return
	 */
	public static <E> String toJsonString(List<E> list) {
		JSONArray jsonArray = JSONArray.fromObject(list);
		return jsonArray.toString();
	}

	/**
	 * 将json数组对象序列化成json文本格式
	 * 
	 * @param jsonArray
	 * @return
	 */
	public static String toJsonString(JSONArray jsonArray) {
		return jsonArray.toString();
	}

	/**
	 * json对象转换为json文本格式
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static String toJsonString(JSONObject jsonObject) {
		return jsonObject.toString();
	}

	/**
	 * 将对象转换成list对象
	 * 
	 * @param json
	 * @return
	 */
	public static List toArrayList(Object obj) {
		JSONArray jsonArray = JSONArray.fromObject(obj);
		Iterator it = jsonArray.iterator();
		List list = Lists.newArrayList();
		while (it.hasNext()) {
			JSONObject jsonObj = (JSONObject) it.next();
			Iterator keys = jsonObj.keys();
			while (keys.hasNext()) {
				Object key = keys.next();
				list.add(jsonObj.get(key));
			}
		}
		return list;
	}

	/**
	 * 将对象转成集合对象
	 * 
	 * @param obj
	 * @return
	 */
	public static Collection toCollection(Object obj) {
		JSONArray jsonArray = JSONArray.fromObject(obj);
		return jsonArray.toCollection(jsonArray);
	}

	/**
	 * 将对象转成json对象数组
	 * 
	 * @param obj
	 * @return
	 */
	public static JSONArray toJSONArray(Object obj) {
		return JSONArray.fromObject(obj);
	}

	/**
	 * 将对象转成Json对象
	 * 
	 * @param obj
	 * @return
	 */
	public static JSONObject toJsonObject(Object obj) {
		return JSONObject.fromObject(obj);
	}

	/**
	 * 将对象转成HashMap
	 * 
	 * @param obj
	 * @return
	 */
	public static HashMap toHashMap(Object obj) {
		HashMap hashMap = Maps.newHashMap();
		JSONObject jsonObj = toJsonObject(obj);
		Iterator it = jsonObj.keys();
		while (it.hasNext()) {
			String key = (String) it.next();
			hashMap.put(key, jsonObj.get(key));
		}
		return hashMap;
	}

	/**
	 * 将对象转成List<Map<String,Object>)
	 * 
	 * @param obj
	 * @return
	 */
	public static List<Map<String, Object>> toList(Object obj) {
		List<Map<String, Object>> list = Lists.newArrayList();
		JSONArray jsonArray = JSONArray.fromObject(obj);
		Iterator it = jsonArray.iterator();
		while (it.hasNext()) {
			JSONObject jsonObj = (JSONObject) it.next();
			Map map = Maps.newHashMap();
			Iterator keys = jsonObj.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				map.put(key, jsonObj.get(key));
			}
			list.add(map);
		}

		return list;
	}

	/**
	 * 将jsonArray 转成指定类型的list
	 * 
	 * @param jsonArray
	 * @param claz
	 * @return
	 */
	public static <T> List<T> toList(JSONArray jsonArray, Class<T> claz) {
		return jsonArray.toList(jsonArray, claz);
	}

	/**
	 * 将对象转成指定类型的list
	 * 
	 * @param obj
	 * @param claz
	 * @return
	 */
	public static <T> List<T> toList(Object obj, Class<T> claz) {
		JSONArray jsonArray = JSONArray.fromObject(obj);
		return JSONArray.toList(jsonArray, claz);
	}

	/**
	 * 将json对象转成指定类型的bean
	 * 
	 * @param jsonObj
	 * @param claz
	 * @return
	 */
	public static <T> T toBean(JSONObject jsonObj, Class<T> claz) {
		return (T) JSONObject.toBean(jsonObj, claz);
	}

	/**
	 * 将对象转成指定类型的bean
	 * 
	 * @param obj
	 * @param claz
	 * @return
	 */
	public static <T> T toBean(Object obj, Class<T> claz) {
		JSONObject jsonObject = JSONObject.fromObject(obj);
		return (T) JSONObject.toBean(jsonObject, claz);
	}

	/**
	 * 将JSON文本反序列化为主从关系的实体
	 * 
	 * @param jsonStr
	 * @param mainClass
	 * @param detailName
	 * @param detailClass
	 * @return
	 */
	public static <T, D> T toBean(String jsonStr, Class<T> mainClass, String detailName, Class<D> detailClass) {
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		JSONArray detailJsonArray = (JSONArray) jsonObject.get(detailName);
		T mainBean = JSONUtil.toBean(jsonObject, mainClass);
		List<D> detailBeans = JSONUtil.toList(detailJsonArray, detailClass);
		try {
			BeanUtils.setProperty(mainBean, detailName, detailBeans);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainBean;

	}

	/**
	 * 将JSON文本反序列化为主从关系的实体
	 * 
	 * @param jsonStr
	 * @param mainClass
	 * @param detailName1
	 * @param detailClass1
	 * @param detailName2
	 * @param detailClass2
	 * @return
	 */
	public static <T, D1, D2> T toBean(String jsonStr, Class<T> mainClass, String detailName1, Class<D1> detailClass1,
			String detailName2, Class<D2> detailClass2) {
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		JSONArray detailJsonArray1 = (JSONArray) jsonObject.get(detailName1);
		JSONArray detailJsonArray2 = (JSONArray) jsonObject.get(detailName2);
		T mainBean = JSONUtil.toBean(jsonObject, mainClass);
		List<D1> detailBeans1 = JSONUtil.toList(detailJsonArray1, detailClass1);
		List<D2> detailBeans2 = JSONUtil.toList(detailJsonArray2, detailClass2);
		try {
			BeanUtils.setProperty(mainBean, detailName1, detailBeans1);
			BeanUtils.setProperty(mainBean, detailName2, detailBeans2);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainBean;

	}

	/**
	 * 将JSON文本反序列化为主从关系的实体
	 * 
	 * @param jsonStr
	 * @param mainClass
	 * @param detailName1
	 * @param detailClass1
	 * @param detailName2
	 * @param detailClass2
	 * @param detailName3
	 * @param detailClass3
	 * @return
	 */
	public static <T, D1, D2, D3> T toBean(String jsonStr, Class<T> mainClass, String detailName1,
			Class<D1> detailClass1, String detailName2, Class<D2> detailClass2, String detailName3,
			Class<D3> detailClass3) {
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		JSONArray detailJsonArray1 = (JSONArray) jsonObject.get(detailName1);
		JSONArray detailJsonArray2 = (JSONArray) jsonObject.get(detailName2);
		JSONArray detailJsonArray3 = (JSONArray) jsonObject.get(detailName3);
		T mainBean = JSONUtil.toBean(jsonObject, mainClass);

		List<D1> detailBeans1 = JSONUtil.toList(detailJsonArray1, detailClass1);
		List<D2> detailBeans2 = JSONUtil.toList(detailJsonArray2, detailClass2);
		List<D2> detailBeans3 = JSONUtil.toList(detailJsonArray2, detailClass2);
		try {
			BeanUtils.setProperty(mainBean, detailName1, detailBeans1);
			BeanUtils.setProperty(mainBean, detailName2, detailBeans2);
			BeanUtils.setProperty(mainBean, detailName3, detailBeans3);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainBean;
	}
	
	/**
	 * 将JSON文本反序列化为主从关系的实体 
	 * @param jsonStr
	 * @param mainClass
	 * @param detailClass
	 * @return
	 */
	public static <T, D1, D2, D3> T toBean(String jsonStr, Class<T> mainClass, HashMap<String, Class> detailClass) {
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		T mainBean = JSONUtil.toBean(jsonObject, mainClass);

		Set<String> detailNames = detailClass.keySet();
		for (String detailName : detailNames) {
			JSONArray detailJsonArray = (JSONArray) jsonObject.get(detailName);
			Class clz = detailClass.get(detailName);

			List detailBeans = JSONUtil.toList(detailJsonArray, clz);
			try {
				BeanUtils.setProperty(mainBean, detailName, detailBeans);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}
		return mainBean;

	}

}
