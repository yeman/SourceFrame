package com.holly.util;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.PropertiesConfiguration;

import lombok.extern.log4j.Log4j;

@Log4j
public class PropertiesUtils {

	private static String DEFAULT_PROPERTIES = "application.properties";

	private static Map<String, PropertiesConfiguration> configs = new ConcurrentHashMap<String, PropertiesConfiguration>();


	public static String getProperty(String key, String... fileNames) {
		String value = null;
		try {
			String propfile = (fileNames.length > 0) ? fileNames[0] : DEFAULT_PROPERTIES;
			value = getPropertiesConfiguration(propfile).getString(key);
		} catch (Exception e) {
			log.error("读取配置文件失败：" + e.getMessage());
		}
		return value;
	}

	public static void setProperty(String key, String value, String... fileNames) {
		try {
			String propfile = (fileNames.length > 0) ? fileNames[0] : DEFAULT_PROPERTIES;
			Properties props = locateFromClasspath(propfile);
			props.setProperty(key, value);
		} catch (Exception e) {
			log.error("写入配置文件失败：" + e.getMessage());
		}
	}

	static Properties locateFromClasspath(String resourceName) throws Exception {
		URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
		Properties prop = new Properties();
		prop.load(url.openStream());
		return prop;
	}

	public static PropertiesConfiguration getPropertiesConfiguration(String resourceName) throws Exception {
		if (configs.containsKey(resourceName)) {
			return configs.get(resourceName);
		} else {
			PropertiesConfiguration config = new PropertiesConfiguration(resourceName);
			configs.put(resourceName, config);
			return config;
		}
	}

}
